export function createComment() {

    const params = new URLSearchParams(window.location.search);
    const postId = params.get("id");
    const url = `/api/posts/${postId}/comments`;
    const contentInput = document.getElementById('comment-content');
    const content = contentInput.value;

    fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({content})
    })
        .then(function(response) {
            return response.json();
        })
        .then(function(data) {
            if (data.status === 'success') {
                contentInput.value = '';
                appendNewComment(data.comment);
            } else {
                alert(data.error);
            }
        })
}

import { isLogined, getLoginMember } from '/js/auth/auth.js';
import { addEditButton, addDeleteButton } from '/js/comment/comment-list.js';

function appendNewComment(comment) {
    const divPost = document.getElementById('comment-list');

    const entity = document.createElement('li');
    entity.className = 'mb-3 pb-3 border-bottom position-relative';
    entity.id = `comment-${comment.id}`;

    const contentDiv = document.createElement('p');
    contentDiv.className = 'mb-1';
    contentDiv.textContent = comment.content;

    const meta = document.createElement('small');
    meta.className = 'text-muted';
    meta.innerHTML = `<strong>${comment.authorName}</strong> · ${comment.createdAt.replace('T', ' ').substring(0, 16)}`;

    const actionDiv = document.createElement('div');
    actionDiv.className = 'position-absolute top-0 end-0 d-flex gap-1';

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
