export function listPosts() {
    fetch('/api/posts', {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
    })
        .then(function(response) {
            return response.json();
        })
        .then(function(data) {
            if (data.status !== 'success') {
                alert('Posts loading failed: ' + data.error);
                return;
            }

            let tBody = document.getElementById('posts-table-body');
            let posts = data.posts;
            tBody.innerHTML = '';

            for (let i = 0; i < posts.length; i++) {
                let post = posts[i];
                let row = document.createElement('tr');

                //title
                let titleCell = document.createElement('td');
                let link = document.createElement('a');
                link.href = '/post/post.html?id=' + post.id;
                link.textContent = post.title;
                titleCell.appendChild(link);
                row.appendChild(titleCell);

                //author
                let authorCell = document.createElement('td');
                authorCell.textContent = post.authorName;
                row.appendChild(authorCell);

                //createAt
                let dateCell = document.createElement('td');
                dateCell.textContent = post.createdAt.split('T')[0]; //"createdAt": "2025-07-30T15:25:41"
                row.appendChild(dateCell);

                tBody.appendChild(row);

            }
        })
}