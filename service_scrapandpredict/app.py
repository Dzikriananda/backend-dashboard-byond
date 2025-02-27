import pandas as pd
import re
import string
import joblib
import uuid
from flask import Flask, request, jsonify
from google_play_scraper import Sort, reviews,app as appp
from nltk.corpus import stopwords
from Sastrawi.Stemmer.StemmerFactory import StemmerFactory
import psycopg2
from psycopg2.extras import execute_values

app = Flask(__name__)

class SentimentPredictor:
    def __init__(self, vectorizer_path, model_path):
        self.tfidf_vectorizer = joblib.load(vectorizer_path)
        self.logistic_model = joblib.load(model_path)
        self.stop_words = stopwords.words('indonesian')
        self.stemmer = StemmerFactory().create_stemmer()

    def preprocess_text(self, text):
        original_text = text
        text = re.sub('[%s]' % re.escape(string.punctuation), '', text)  # Remove punctuation
        text = re.sub('\d+', '', text)  # Remove digits
        text = ' '.join([word for word in text.split() if word not in self.stop_words])  # Remove stopwords
        text = self.stemmer.stem(text)  # Stemming
        return original_text, text

    def predict(self, texts):
        preprocessed_texts = [self.preprocess_text(text)[1] for text in texts]
        vectorized_texts = self.tfidf_vectorizer.transform(preprocessed_texts)
        return self.logistic_model.predict(vectorized_texts)

# Initialize Sentiment Predictor
predictor = SentimentPredictor('tfidf_vectorizer.pkl', 'logistic_model.pkl')






@app.route('/fetch_reviews', methods=['GET'])
def fetch_reviews():
    try:
        data = request.json
        print('/FETCH REVIEW IS CALLED')
        if 'text' in data:  # If single review is provided
            text = data['text']
            original_text, preprocessed_text = predictor.preprocess_text(text)
            sentiment = predictor.predict([text])[0]

            return jsonify({'text': original_text, 'preprocessed_text': preprocessed_text, 'predicted_sentiment': sentiment})

        else:  # Fetch Google Play reviews
            result, _ = reviews(
                'co.id.bankbsi.superapp',
                lang='id',
                country='id',
                count=500,
                sort=Sort.NEWEST,
                filter_score_with=None
            )

            df = pd.DataFrame(result)
            df = df[['reviewId', 'userName', 'userImage', 'content', 'score', 'at', 'thumbsUpCount', 'appVersion']]
            df['content'] = df['content'].fillna('')  # Handle missing values

            # Preprocess content and predict sentiment
            df['original_content'], df['preprocessed_content'] = zip(*df['content'].apply(predictor.preprocess_text))
            df['sentiment'] = predictor.predict(df['preprocessed_content'].astype(str))

            # Convert reviewId to UUID format
            df['reviewId'] = df['reviewId'].apply(lambda x: str(uuid.UUID(x)))

            # Connect to PostgreSQL database
            conn = psycopg2.connect(
                host="db",
                port=5432,
                database="multimatics-backend",
                user="userDb_1234_Multimatics",
                password="Tniabri12!!__0Toqum"  
            )
            print("Connected to the database!")  # <-- Add this after connection
            cursor = conn.cursor()

            # Check for existing reviews and filter out duplicates
            existing_review_ids_query = 'SELECT "review_id" FROM byond_review WHERE "review_id" = ANY(%s::uuid[])'
            cursor.execute(existing_review_ids_query, (df['reviewId'].tolist(),))
            existing_review_ids = {row[0] for row in cursor.fetchall()}

            new_reviews = df[~df['reviewId'].isin(existing_review_ids)]

            if not new_reviews.empty:
                print('inserting')
                insert_query = """
                INSERT INTO byond_review (review_id, user_name, user_image, content, preprocessed_content, score, at, thumbs_up_count, app_version, sentiment)
                VALUES %s
                """
                execute_values(cursor, insert_query, 
                               [(str(uuid.UUID(row[0])), *row[1:]) for row in new_reviews[['reviewId', 'userName', 'userImage', 'content', 'preprocessed_content', 'score', 'at', 'thumbsUpCount', 'appVersion', 'sentiment']].values.tolist()])
                conn.commit()
                print('inserted')

            cursor.close()
            conn.close()
            return jsonify({'number of reviews inserted': len(new_reviews)})

    except Exception as e:
        return jsonify({'error': str(e)}), 500
    

@app.route('/fetch_app_details', methods=['GET'])
def fetch_app_details():
    try:
        result = appp(
            'co.id.bankbsi.superapp',
            lang='id',  # defaults to 'en'
            country='id'  # defaults to 'us'
        )
        app_data = {
            'App Score': result['score'],
            'App Downloads': result['installs'],
            'Number of Reviews': result['reviews'],
        }
        return jsonify(app_data)
    except Exception as e:
        return jsonify({'error': str(e)}), 500


if __name__ == '__main__':
    app.run(debug=True)
