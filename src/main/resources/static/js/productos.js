const token = localStorage.getItem("token");

const form = document.getElementById("formProducto");

form.addEventListener("submit", async function (e) {
    e.preventDefault();

    const nombre = document.getElementById("nombre").value;
    const precio = document.getElementById("precio").value;
    const stock = document.getElementById("stock").value;
    const categoria = document.getElementById("categoria").value;

    try {
        const response = await fetch("http://localhost:8080/api/productos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify({
                nombre: nombre,
                precio: precio,
                stock: stock,
                categoria:categoria
            })
        });

        if (!response.ok) {
            throw new Error("Error al crear producto");
        }

        alert("Producto creado correctamente");

        location.reload();

    } catch (error) {
        console.error(error);
        alert(error.message);
    }
});

if (!token) {
    window.location.href = "login.html";
}

fetch("http://localhost:8080/api/productos", {
    method: "GET",
    headers: {
        "Authorization": "Bearer " + token
    }
})



    .then(response => {
        if (!response.ok) {
            throw new Error("No autorizado o error cargando productos");
        }
        return response.json();
    })
    .then(data => {
        console.log("DATA:", data);

        const tabla = document.getElementById("tablaProductos");

        data.forEach(producto => {
            tabla.innerHTML += `
        <tr>
            <td>${producto.id}</td>
            <td>${producto.nombre}</td>
            <td>${producto.precio}</td>
            <td>${producto.stock}</td>
            <td>${producto.categoria}</td>
            <td>
                <button onclick="editarProducto(${producto.id})">Editar</button>
                <button onclick="eliminarProducto(${producto.id})">Ocultar</button>
            </td>
        </tr>
    `;
        });
    })
    .catch(error => {
        console.error(error);
        alert(error.message);
    });
async function eliminarProducto(id) {
    const confirmar = confirm("¿Eliminar producto?");

    if (!confirmar) return;

    try {
        const response = await fetch(`http://localhost:8080/api/productos/${id}`, {
            method: "DELETE",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            throw new Error("Error al eliminar producto");
        }

        alert("Producto eliminado");
        location.reload();

    } catch (error) {
        alert(error.message);
    }
}

async function editarProducto(id) {
    const nombre = prompt("Nuevo nombre:");
    const precio = prompt("Nuevo precio:");
    const stock = prompt("Nuevo stock:");
    const categoria = prompt("Nueva categoría:");

    if (!nombre || !precio || !stock || !categoria) {
        alert("Todos los campos son obligatorios");
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/productos/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify({
                nombre,
                precio: parseFloat(precio),
                stock: parseInt(stock),
                categoria
            })
        });

        if (!response.ok) {
            throw new Error("Error al editar producto");
        }

        alert("Producto actualizado");
        location.reload();

    } catch (error) {
        alert(error.message);
    }
}
async function restaurarProducto(id) {
    try {
        const response = await fetch(`http://localhost:8080/api/productos/restaurar/${id}`, {
            method: "PUT",
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if (!response.ok) {
            throw new Error("Error al restaurar producto");
        }

        alert("Producto restaurado");
        location.reload();

    } catch (error) {
        alert(error.message);
    }
}