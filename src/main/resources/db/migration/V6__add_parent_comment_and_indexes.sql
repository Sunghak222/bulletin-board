-- Add self-referencing column for replies
ALTER TABLE comment ADD COLUMN parent_id BIGINT;

-- FK: parent -> comment(id)
ALTER TABLE comment
    ADD CONSTRAINT fk_comment_parent
        FOREIGN KEY (parent_id) REFERENCES comment(id)
            ON DELETE SET NULL; -- (부모 삭제 시 자식의 parent_id를 NULL로)

-- 조회 최적화 인덱스
CREATE INDEX idx_comment_post_parent_created_at
    ON comment (post_id, parent_id, created_at);

-- 부모별 대댓글 조회 최적화(선택)
CREATE INDEX idx_comment_parent_id
    ON comment (parent_id);