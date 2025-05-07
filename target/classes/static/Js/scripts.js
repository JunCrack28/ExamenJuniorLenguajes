// scripts.js

// Mostrar mensaje de error si existe
document.addEventListener('DOMContentLoaded', () => {
    const errorMessage = document.getElementById('errorMessage');
    if (errorMessage && errorMessage.textContent) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: errorMessage.textContent,
            confirmButtonColor: '#1a3c6e'
        });
    }
});

async function confirmDeleteBarco(button) {
    const barcoId = button.getAttribute('data-id');
    Swal.fire({
        title: '¿Estás seguro?',
        text: '¿Quieres eliminar este barco?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#1a3c6e',
        cancelButtonColor: '#dc3545',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then(async (result) => {
        if (result.isConfirmed) {
            try {
                const response = await fetch(`/api/barcos/${barcoId}`, {
                    method: 'DELETE'
                });
                if (response.ok) {
                    Swal.fire(
                        '¡Eliminado!',
                        'El barco ha sido eliminado.',
                        'success'
                    ).then(() => {
                        window.location.reload();
                    });
                } else {
                    const errorText = await response.text();
                    Swal.fire(
                        'Error',
                        errorText || 'No se pudo eliminar el barco.',
                        'error'
                    );
                }
            } catch (error) {
                Swal.fire(
                    'Error',
                    'Ocurrió un error al intentar eliminar el barco.',
                    'error'
                );
            }
        }
    });
}

async function showContenedores(button) {
    const barcoId = button.getAttribute('data-id');
    const barcoNombre = button.getAttribute('data-nombre');
    
    const response = await fetch(`/api/contenedores?barcoId=${barcoId}`);
    const contenedores = await response.json();
    
    document.getElementById('barcoId').value = barcoId;
    document.getElementById('barcoNombre').textContent = barcoNombre;
    const tbody = document.getElementById('contenedoresBody');
    tbody.innerHTML = '';
    
    contenedores.forEach(cont => {
        const row = `<tr>
            <td>${cont.id}</td>
            <td>${cont.vin}</td>
            <td>${cont.fecha}</td>
            <td>
                <button class="btn btn-sm btn-warning me-1" onclick="editContenedor(${cont.id}, '${cont.vin}', '${cont.fecha}', ${barcoId})">Editar</button>
                <button class="btn btn-sm btn-danger me-1" onclick="deleteContenedor(${cont.id}, ${barcoId})">Eliminar</button>
            </td>
        </tr>`;
        tbody.innerHTML += row;
    });
    
    document.getElementById('contenedorSection').style.display = 'block';
}

async function deleteContenedor(id, barcoId) {
    Swal.fire({
        title: '¿Estás seguro?',
        text: '¿Quieres eliminar este contenedor?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#1a3c6e',
        cancelButtonColor: '#dc3545',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then(async (result) => {
        if (result.isConfirmed) {
            await fetch(`/api/contenedores/${id}`, { method: 'DELETE' });
            const button = document.querySelector(`button[data-id="${barcoId}"]`);
            showContenedores(button);
            Swal.fire(
                '¡Eliminado!',
                'El contenedor ha sido eliminado.',
                'success'
            );
        }
    });
}




function editContenedor(id, vin, fecha, barcoId) {
    document.getElementById('contenedorForm').reset();
    document.getElementById('contenedorId').value = id;
    document.querySelector('input[name="vin"]').value = vin;
    document.querySelector('input[name="fecha"]').value = fecha;
    document.getElementById('barcoId').value = barcoId;
}
