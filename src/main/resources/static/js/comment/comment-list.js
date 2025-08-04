import { isLogined, getLoginMember } from '/js/auth/auth.js';
import { deleteComment } from '/js/comment/delete.js';

export function displayCommentList() {
    const params = new URLSearchParams(window.location.search);
    const postId = params.get("id");
    const url = `/api/posts/${postId}/comments`;

    fetch(url, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    })
        .then(function(response) {
            return response.json();
        })
        .then(function(data) {
            //data.error is not implemented in controller
            if (data.status !== "success") {
                alert('Posts loading failed: ' + data.error);
            }

            let divPost = document.getElementById('comment-list');
            divPost.innerHTML = '';
            let comments = data.comments;

            for (let i = 0; i < comments.length; i++) {
                let comment = comments[i];
                let entity = document.createElement('li');
                entity.className = 'mb-3 pb-3 border-bottom position-relative';
                entity.id = `comment-${comment.id}`;

// 댓글 본문
                let contentDiv = document.createElement('p');
                contentDiv.className = 'mb-1';
                contentDiv.textContent = comment.content;

// 작성자 및 날짜
                let meta = document.createElement('small');
                meta.className = 'text-muted';
                meta.innerHTML = `<strong>${comment.authorName}</strong> · ${comment.createdAt.replace('T', ' ').substring(0, 16)}`;

// 버튼 영역 (Edit/Delete 우측 상단 배치)
                let actionDiv = document.createElement('div');
                actionDiv.className = 'position-absolute top-0 end-0 d-flex gap-1'; // Changed

// append
                entity.appendChild(contentDiv);
                entity.appendChild(meta);
                entity.appendChild(actionDiv);
                divPost.appendChild(entity);

                isLogined().then(loggedIn => {
                    if (!loggedIn) return;
                    getLoginMember().then(loginMember => {
                        if (loginMember && loginMember.id === comment.authorId) {
                            addEditButton(comment, contentDiv, actionDiv);
                            addDeleteButton(comment, actionDiv);
                        }
                    });
                });

            }
        })
}

export function addEditButton(comment, contentDiv, actionDiv) {
    const updateBtn = document.createElement('button');
    updateBtn.textContent = 'Edit';
    updateBtn.className = 'btn btn-sm btn-outline-secondary me-1';
    actionDiv.appendChild(updateBtn);

    updateBtn.onclick = () => {
        const originalText = contentDiv.textContent;

        const textarea = document.createElement('textarea');
        textarea.className = 'form-control mb-2';
        textarea.value = originalText;

        const saveBtn = document.createElement('button');
        saveBtn.textContent = 'Save';
        saveBtn.className = 'btn btn-sm btn-outline-dark me-1';

        const cancelBtn = document.createElement('button');
        cancelBtn.textContent = 'Cancel';
        cancelBtn.className = 'btn btn-sm btn-outline-secondary';

        saveBtn.onclick = () => {
            const updatedText = textarea.value;
            fetch (`/api/comments/${comment.id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ content: updatedText})
            })
                .then(function(response) {
                    return response.json();
                })
                .then(function(data) {
                    if (data.status === 'success') {
                        contentDiv.textContent = updatedText;
                        actionDiv.innerHTML = '';
                        addEditButton(comment, contentDiv, actionDiv);
                        addDeleteButton(comment, actionDiv);
                    } else {
                        alert(data.message);
                    }
                })
        };

        cancelBtn.onclick = () => {
            actionDiv.innerHTML = '';
            contentDiv.textContent = originalText;
            addEditButton(comment, contentDiv, actionDiv);
            addDeleteButton(comment, actionDiv);
        };

        contentDiv.innerHTML = '';
        contentDiv.appendChild(textarea);
        actionDiv.innerHTML = '';
        actionDiv.appendChild(saveBtn);
        actionDiv.appendChild(cancelBtn);
    };
}
export function addDeleteButton(comment, actionDiv) {
    const deleteButton = document.createElement('button');
    deleteButton.textContent = 'Delete';
    deleteButton.className = 'btn btn-sm btn-outline-danger';
    deleteButton.addEventListener('click', () => {
        deleteComment(comment.id)
    });
    actionDiv.appendChild(deleteButton);
}