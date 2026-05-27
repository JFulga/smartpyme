const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}

const API_URL = "https://smartpyme-d5rl.onrender.com";

async function cargarDashboard() {

    // VENTAS
    const ventasResponse = await fetch(`${API_URL}/api/ventas`, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });


}

    const ventas = await ventasResponse.json();

    let totalVentas = 0;
    ventas.forEach(v => totalVentas += v.total);

    document.getElementById("totalVentas").innerText = `$${totalVentas.toFixed(2)}`;


    // CLIENTES
    const clientesResponse = await fetch(`${API_URL}/api/clientes`, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    const clientes = await clientesResponse.json();

    document.getElementById("totalClientes").innerText = clientes.length;



    // PRODUCTOS
    const productosResponse = await fetch(`${API_URL}/api/productos`, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    const productos = await productosResponse.json();

    document.getElementById("totalProductos").innerText = productos.length;
    // STOCK BAJO
    const bajoStock = productos.filter(p => p.stock <= 5);

    document.getElementById("stockBajo").innerText = bajoStock.length;


cargarDashboard();