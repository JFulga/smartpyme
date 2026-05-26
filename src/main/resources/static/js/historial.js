const token = localStorage.getItem("token");

const tablaVentas = document.getElementById("tablaVentas");
const API_URL = "https://smartpyme-d5rl.onrender.com";

async function cargarVentas(){

    try {

        const response = await fetch(
            `${API_URL}/api/ventas`,
            {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        const ventas = await response.json();

        console.log(ventas);

        tablaVentas.innerHTML = "";

        ventas.forEach(venta => {

            let detallesHTML = "";

            venta.detalles.forEach(detalle => {

                detallesHTML += `
        <li>
            ${detalle.producto.nombre}
            - Cantidad: ${detalle.cantidad}
            - $${detalle.precioUnitario}
        </li>
    `;
            });

            tablaVentas.innerHTML += `
                <tr>

                    <td>${venta.id}</td>

                    <td>
                        ${new Date(venta.fecha)
                .toLocaleString()}
                    </td>

                    <td>
    ${
                venta.cliente
                    ? `${venta.cliente.nombre} ${venta.cliente.apellido}`
                    : "Cliente no registrado"
            }
</td>
                    <td>
                        $${venta.total.toFixed(2)}
                    </td>

                    <td>
                        <ul>
                            ${detallesHTML}
                        </ul>
                    </td>

                    <td>

                        <button
                            class="btn btn-danger btn-sm"
                            onclick="eliminarVenta(${venta.id})"
                        >
                            Eliminar
                        </button>

                    </td>

                </tr>
            `;
        });

    } catch(error){

        console.error(error);
    }
}

async function eliminarVenta(id){

    const confirmar = confirm(
        "¿Eliminar esta venta?"
    );

    if(!confirmar) return;

    try {

        const response = await fetch(
            `${API_URL}/api/ventas/${id}`,
            {
                method: "DELETE",

                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        if(response.ok){

            alert("Venta eliminada");

            cargarVentas();

        } else {

            const error = await response.text();

            alert(error);
        }

    } catch(error){

        console.error(error);
    }
}

cargarVentas();