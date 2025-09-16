import { isLogined, getLoginMember } from '/js/auth/auth.js';
import { deleteComment } from '/js/comment/delete.js';
import {appendNewComment} from "./create.js";

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

            const byId = new Map(); //{CommentId : Comment Object]
            const childrenMap = new Map(); //{parentId : childId}
            const roots = []; // root comments

            for (const c of comments) {
                byId.set(c.id, c);
                const key = c.parentId ?? 0;
                if (!childrenMap.has(key)) childrenMap.set(key, []);
                childrenMap.get(key).push(c);
            }
            if (childrenMap.has(0)) {
                roots.push(...childrenMap.get(0));
            }

            function renderComment(c, depth=0) {
                const entity = document.createElement('li');
                entity.className = 'mb-3 pb-3 border-bottom position-relative';
                if (depth > 0) entity.classList.add('ms-4');
                entity.id = `comment-${c.id}`;

                const contentDiv = document.createElement('p');
                contentDiv.className = 'mb-1';
                contentDiv.innerHTML = c.content.replace(/\n/g, '<br>');

                const meta = document.createElement('small');
                meta.className = 'text-muted';
                const createdAt = c.createdAt.replace('T', ' ').substring(0, 16);
                meta.innerHTML = `<strong>${c.authorName}</strong> · ${createdAt}`;

                const actionDiv = document.createElement('div');
                actionDiv.className = 'position-absolute top-0 end-0 d-flex gap-1';

                entity.appendChild(contentDiv);
                entity.appendChild(meta);
                entity.appendChild(actionDiv);
                divPost.appendChild(entity);

                isLogined().then(loggedIn => {
                    if (!loggedIn) return;
                    getLoginMember().then(loginMember => {
                        if (loginMember && loginMember.id === c.authorId) {
                            addEditButton(c, contentDiv, actionDiv);
                            addDeleteButton(c, actionDiv);
                        }
                        if (c.parentId == null) {
                            addReplyButton(c, actionDiv);
                        }
                    });
                });
                const children = childrenMap.get(c.id) || [];
                for (const child of children) renderComment(child, depth + 1);
            }
            for (const root of roots) renderComment(root, 0);
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

function openReplyForm(parentId) {
    const parentLi = document.getElementById(`comment-${parentId}`);

    const formDiv = document.createElement('div');
    formDiv.id = `reply-form-${parentId}`;
    formDiv.className = 'mt-2';

    const textarea = document.createElement('textarea');
    textarea.className = 'form-control mb-2';
    textarea.rows = 2; //height of the txtarea
    textarea.placeholder = 'Reply here';

    const submitBtn = document.createElement('button');
    submitBtn.className = 'btn btn-sm btn-primary';
    submitBtn.textContent = 'Send';

    submitBtn.onclick = () => {
        const content = textarea.value.trim();
        if (!content) {
            alert("The comment is blank.");
            return;
        }

        const postId = new URLSearchParams(window.location.search).get("id");

        fetch(`/api/posts/${postId}/comments`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                content: content,
                parentId: parentId
            })
        })
            .then(function(response) {
                return response.json();
            })
            .then(data => {
                if (data.status === "success") {
                    appendNewComment(data.comment);
                    formDiv.remove();
                } else {
                    alert("Reply Comment Failed: " + (data.error ?? 'unknown'));
                }
            })
    }

    formDiv.appendChild(textarea);
    formDiv.appendChild(submitBtn);

    parentLi.appendChild(formDiv);
}
export function addReplyButton(comment, actionDiv) {
    const replyBtn = document.createElement('button');
    replyBtn.innerText = 'Reply';
    replyBtn.className = 'btn btn-sm btn-outline-secondary me-1';

    replyBtn.onclick = () => openReplyForm(comment.id);

    actionDiv.append(replyBtn);
}
