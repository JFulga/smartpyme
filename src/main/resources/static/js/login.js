const form = document.getElementById("loginForm");

form.addEventListener("submit", async function (e) {
    e.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const mensaje = document.getElementById("mensaje");

    const API_URL = "https://smartpyme-d5rl.onrender.com";

    const response = await fetch(`${API_URL}/auth/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    });

    if (!response.ok) {
        throw new Error("Credenciales incorrectas");
    }

        const data = await response.json();

        localStorage.setItem("token", data.token);

        window.location.href = "dashboard.html";

    } catch (error) {
        mensaje.innerHTML = `
            <div class="alert alert-danger">
                ${error.message}
            </div>
        `;
    }
});