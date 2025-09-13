CREATE TABLE post_reaction (
  id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  post_id   BIGINT NOT NULL,
  member_id BIGINT NOT NULL,
  type      VARCHAR(10) NOT NULL, --('LIKE' | 'DISLIKE')
  CONSTRAINT uq_post_member UNIQUE (post_id, member_id), -- 각 포스트/멤버별로 유일성
  CONSTRAINT fk_react_post   FOREIGN KEY (post_id)   REFERENCES post(id)   ON DELETE CASCADE,
  CONSTRAINT fk_react_member FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE
);