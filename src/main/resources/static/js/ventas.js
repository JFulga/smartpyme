const token = localStorage.getItem("token");

const API_URL = "https://smartpyme-d5rl.onrender.com";

const clienteSelect = document.getElementById("clienteSelect");
const productoSelect = document.getElementById("productoSelect");
const cantidadInput = document.getElementById("cantidad");

const tablaCarrito = document.getElementById("tablaCarrito");

const totalVenta = document.getElementById("totalVenta");

let carrito = [];


// =========================
// CARGAR CLIENTES
// =========================

async function cargarClientes(){

    const response = await fetch(
        `${API_URL}/api/clientes`,
        {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        }
    );

    const clientes = await response.json();

    clientes.forEach(cliente => {

        clienteSelect.innerHTML += `
            <option value="${cliente.id}">
                ${cliente.nombre} ${cliente.apellido}
            </option>
        `;
    });
}


// =========================
// CARGAR PRODUCTOS
// =========================

async function cargarProductos(){

    const response = await fetch(
        `${API_URL}/api/productos`,
        {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        }
    );

    const productos = await response.json();

    productos.forEach(producto => {

        productoSelect.innerHTML += `
            <option 
                value="${producto.id}"
                data-precio="${producto.precio}"
            >
                ${producto.nombre}
            </option>
        `;
    });
}


// =========================
// AGREGAR PRODUCTO
// =========================

document.getElementById("btnAgregar")
    .addEventListener("click", function(){

        const productoId = productoSelect.value;

        const productoNombre =
            productoSelect.options[
                productoSelect.selectedIndex
                ].text;

        const precio = parseFloat(
            productoSelect.options[
                productoSelect.selectedIndex
                ].dataset.precio
        );

        const cantidad = parseInt(
            cantidadInput.value
        );

        if(isNaN(cantidad) || cantidad <= 0){

            alert("Cantidad inválida");

            return;
        }

        const existente = carrito.find(
            item => item.productoId === parseInt(productoId)
        );

        if(existente){

            existente.cantidad += cantidad;

        } else {

            carrito.push({
                productoId: parseInt(productoId),
                productoNombre: productoNombre,
                cantidad: cantidad,
                precio: precio
            });
        }

        renderizarCarrito();

        cantidadInput.value = "";
    });


// =========================
// MOSTRAR CARRITO
// =========================

function renderizarCarrito(){

    tablaCarrito.innerHTML = "";

    let total = 0;

    carrito.forEach((item, index) => {

        const subtotal =
            item.precio * item.cantidad;

        total += subtotal;

        tablaCarrito.innerHTML += `
            <tr>

                <td>${item.productoNombre}</td>

                <td>${item.cantidad}</td>

                <td>$${item.precio.toFixed(2)}</td>

                <td>$${subtotal.toFixed(2)}</td>

                <td>

                    <button
                        class="btn btn-warning btn-sm"
                        onclick="editarCantidad(${index})"
                    >
                        Editar
                    </button>

                    <button
                        class="btn btn-danger btn-sm"
                        onclick="eliminarProducto(${index})"
                    >
                        Eliminar
                    </button>

                </td>

            </tr>
        `;
    });

    totalVenta.innerText =
        `Total: $${total.toFixed(2)}`;
}


// =========================
// ELIMINAR PRODUCTO
// =========================

function eliminarProducto(index){

    carrito.splice(index, 1);

    renderizarCarrito();
}


// =========================
// EDITAR CANTIDAD
// =========================

function editarCantidad(index){

    const nuevaCantidad = prompt(
        "Nueva cantidad:",
        carrito[index].cantidad
    );

    if(nuevaCantidad === null){

        return;
    }

    const cantidad = parseInt(nuevaCantidad);

    if(isNaN(cantidad) || cantidad <= 0){

        alert("Cantidad inválida");

        return;
    }

    carrito[index].cantidad = cantidad;

    renderizarCarrito();
}


// =========================
// GUARDAR VENTA
// =========================

document.getElementById("btnGuardarVenta")
    .addEventListener("click", async function(){

        if(carrito.length === 0){

            alert("El carrito está vacío");

            return;
        }

        const venta = {

            clienteId: parseInt(
                clienteSelect.value
            ),

            productos: carrito.map(item => ({
                productoId: item.productoId,
                cantidad: item.cantidad
            }))
        };

        try {

            const response = await fetch(
                `${API_URL}/api/ventas`,
                {
                    method: "POST",

                    headers: {
                        "Authorization": `Bearer ${token}`,
                        "Content-Type": "application/json"
                    },

                    body: JSON.stringify(venta)
                }
            );

            if(response.ok){

                alert("Venta registrada");

                carrito = [];

                renderizarCarrito();

            } else {

                const error =
                    await response.text();

                alert(error);
            }

        } catch(error){

            console.error(error);

            alert("Error al guardar la venta");
        }
    });


// =========================
// INICIAR
// =========================

cargarClientes();
cargarProductos();