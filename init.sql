CREATE TABLE users (
	id serial4 NOT NULL,
	username varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	"password" varchar(255) NOT NULL,
	created_at timestamptz DEFAULT now() NULL,
	updated_at timestamptz DEFAULT now() NULL,
	CONSTRAINT users_email_key UNIQUE (email),
	CONSTRAINT users_pkey PRIMARY KEY (id),
	CONSTRAINT users_username_key UNIQUE (username)
);

CREATE TABLE byond_review (
	review_id UUID UNIQUE NOT NULL,  -- Unique constraint instead of primary key
	user_name VARCHAR(255),
	user_image TEXT,
	content TEXT,
	score INT CHECK (score >= 1 AND score <= 5),
	at TIMESTAMP,
	sentiment varchar(1),
	preprocessed_content TEXT,
	thumbs_up_count Int,
	app_version Varchar(10)
);

INSERT INTO byond_review
(review_id, user_name, content, score, at, thumbs_up_count, sentiment)
VALUES
('c6e75cb1-10eb-4c51-8062-b1dd7a8cb95b', 'siti Novi', 'Dah sebulan masih gagal terus verivikasi', 1, '2025-02-24 17:14:45.000', 0, '0');

INSERT INTO byond_review
(review_id, user_name, content, score, at, thumbs_up_count, app_version, sentiment)
VALUES
('1aeb1eb7-91f6-400d-a147-c33dafc0890e', 'Gusro Ali Wardana',
'Tolong di perhatikan lagi masalah ngelag atau apa itu, terkadang masuk gabisa cuma nyetak di gambar byond nya aja gabisa login, pas saya lgi butuh untuk bayar sesuatu malah gabisa login, maaf saya tidak bermaksud untuk menjelek jelek kn aplikasi ini semoga secepatnya di perbaiki dan lebih baik lagi dan tidak lgi bermasalah saat login terimakasih🙏',
5, '2025-02-24 17:13:25.000', 0, '1.0.4', '0');



