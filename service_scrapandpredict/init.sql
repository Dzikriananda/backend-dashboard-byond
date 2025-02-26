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
