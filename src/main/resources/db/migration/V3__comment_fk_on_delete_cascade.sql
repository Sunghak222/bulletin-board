ALTER TABLE comment
    ADD CONSTRAINT fk_comment_post
        FOREIGN KEY (post_id) REFERENCES post(id)
            ON DELETE CASCADE;