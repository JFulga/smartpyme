const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}

const form = document.getElementById("formCliente");

const tabla = document.getElementById("tablaClientes");

const API_URL = "http://localhost:8080/api/clientes";

let clienteEditando = null;


// LISTAR CLIENTES

async function listarClientes() {

    try {

        const response = await fetch(API_URL, {

            method: "GET",

            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }

        });

        const clientes = await response.json();

        tabla.innerHTML = "";

        clientes.forEach(cliente => {

            tabla.innerHTML += `
                <tr>
                    <td>${cliente.id}</td>
                    <td>${cliente.nombre}</td>
                    <td>${cliente.apellido}</td>
                    <td>${cliente.cedula}</td>
                    <td>${cliente.telefono}</td>

                    <td>

                        <button 
                            class="btn btn-warning btn-sm"
                            onclick="editarCliente(
                                ${cliente.id},
                                '${cliente.nombre}',
                                '${cliente.apellido}',
                                ${cliente.cedula},
                                '${cliente.telefono}'
                            )"
                        >
                            Editar
                        </button>

                        <button 
                            class="btn btn-danger btn-sm"
                            onclick="eliminarCliente(${cliente.id})"
                        >
                            Eliminar
                        </button>

                    </td>
                </tr>
            `;
        });

    } catch (error) {

        console.error("Error al listar clientes", error);
    }
}


// EDITAR CLIENTE

function editarCliente(id, nombre, apellido, cedula, telefono){

    document.getElementById("nombre").value = nombre;
    document.getElementById("apellido").value = apellido;
    document.getElementById("cedula").value = cedula;
    document.getElementById("telefono").value = telefono;

    clienteEditando = id;
}


// GUARDAR O ACTUALIZAR CLIENTE

form.addEventListener("submit", async function (e) {

    e.preventDefault();

    const cliente = {

        nombre: document.getElementById("nombre").value,

        apellido: document.getElementById("apellido").value,

        cedula: parseInt(document.getElementById("cedula").value),

        telefono: document.getElementById("telefono").value
    };

    try {

        const response = await fetch(

            clienteEditando
                ? `${API_URL}/${clienteEditando}`
                : API_URL,

            {

                method: clienteEditando ? "PUT" : "POST",

                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(cliente)
            }

        );

        if(response.ok){

            alert(
                clienteEditando
                    ? "Cliente actualizado correctamente"
                    : "Cliente guardado correctamente"
            );

            form.reset();

            clienteEditando = null;

            listarClientes();

        } else {

            alert("Error al guardar cliente");
        }

    } catch (error) {

        console.error("Error:", error);
    }

});


// ELIMINAR CLIENTE

async function eliminarCliente(id){

    const confirmar = confirm("¿Deseas eliminar este cliente?");

    if(!confirmar) return;

    try {

        const response = await fetch(`${API_URL}/${id}`, {

            method: "DELETE",

            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if(response.ok){

            alert("Cliente eliminado");

            listarClientes();

        } else {

            alert("Error al eliminar");
        }

    } catch (error) {

        console.error("Error:", error);
    }
}


// CARGAR CLIENTES AL ABRIR

listarClientes();