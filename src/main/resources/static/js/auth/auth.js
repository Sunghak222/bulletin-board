export async function isLogined() {
    const response = await fetch('/api/members/me');
    const data = await response.json();
    return data.status === 'success';
}

export async function getLoginMember() {
    const response = await fetch('/api/members/me');
    const data = await response.json();
    if (data.status === 'success') {
        return data.loginMember;
    } else {
        return null;
    }
}

import { logout } from '/js/member/logout.js';

export function updateAuthButtons() {
    const authArea = document.getElementById('auth-buttons');
    authArea.innerHTML = '';

    isLogined().then(function(loggedIn) {
        if (loggedIn) {
            const logoutBtn = document.createElement('button');
            logoutBtn.textContent = 'Logout';
            logoutBtn.className = 'btn btn-outline-light me-2';
            logoutBtn.onclick = logout;
            authArea.appendChild(logoutBtn);
        } else {
            const loginLink = document.createElement('a');
            loginLink.href = '/member/login.html';
            loginLink.textContent = 'Login';
            loginLink.className = 'btn btn-outline-light me-2';
            authArea.appendChild(loginLink);

            const registerLink = document.createElement('a');
            registerLink.href = '/member/register.html';
            registerLink.textContent = 'Register';
            registerLink.className = 'btn btn-light';
            authArea.appendChild(registerLink);
        }
    });
}