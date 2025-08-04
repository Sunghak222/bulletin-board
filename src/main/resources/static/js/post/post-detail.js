export function displayPost() {
    const params = new URLSearchParams(window.location.search);
    const postId = params.get("id");
    const url = `/api/posts/${postId}`;

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

            let divPost = document.getElementById('post-section');
            let post = data.post;
            divPost.innerHTML = '';

            let title = document.createElement('h2');
            title.textContent = post.title;
            title.className = 'card-title fw-bold mb-3';
            title.textContent = post.title;
            title.id = 'post-title';
            divPost.appendChild(title);

            let author = document.createElement('h6');
            author.textContent = `Author: ${post.authorName}`;
            author.className = 'card-subtitle mb-2 text-muted';
            author.id = 'post-author';
            divPost.appendChild(author);

            let date = document.createElement('h6');
            date.textContent = `Date: ${post.createdAt.split('T')[0]}`;
            date.className = 'card-subtitle mb-3 text-muted';
            date.id = 'post-date';
            divPost.appendChild(date);

            let content = document.createElement('p');
            content.textContent = post.content;
            content.className = 'card-text fs-5 mt-4';
            content.id = 'post-content';
            divPost.appendChild(content);

            let postActionsDiv = document.createElement('div');
            postActionsDiv.id = 'post-actions';
            postActionsDiv.className = 'mt-4 d-flex gap-2';

            let postCardBody = document.createElement('div');
            postCardBody.className = 'card-body';
            postCardBody.append(title, author, date, content, postActionsDiv);

            let postCard = document.createElement('div');
            postCard.className = 'card shadow-sm mb-4';
            postCard.appendChild(postCardBody);

            divPost.appendChild(postCard);

            addUpdateDeleteButtons(post);
        })
}

import { getLoginMember } from '/js/auth/auth.js';
import { deletePost } from '/js/post/delete.js';

async function addUpdateDeleteButtons(post) {
    const loginMember = await getLoginMember();

    if (!loginMember || loginMember.id !== post.authorId) return;
    const container = document.getElementById('post-actions');
    if (!container) return;

    const editBtn = document.createElement('button');
    editBtn.textContent = 'Edit';
    editBtn.className = 'btn btn-outline-primary';
    editBtn.onclick = () => {
        window.location.href = `/post/update.html?id=${post.id}`;
    };

    const deleteBtn = document.createElement('button');
    deleteBtn.textContent = 'Delete';
    deleteBtn.className = 'btn btn-danger';
    deleteBtn.onclick = () => deletePost(post.id);

    container.appendChild(editBtn);
    container.appendChild(deleteBtn);
}