package client.i18n;

import java.util.ListResourceBundle;

public class Messages_es_CO extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][]{

                // ==========================================
                // 1. GENERAL Y UI (Pestañas, Temas, Consola)
                // ==========================================
                { "tab.workers",              "Trabajadores" },
                { "tab.map",                  "Mapa Visual" },
                { "theme.light",              "☀ Modo Claro" },
                { "theme.dark",               "🌙 Modo Oscuro" },
                { "title.console",            "Consola de Sistema" },
                { "status.ready",             "Sistema listo." },

                // ==========================================
                // 2. INICIO DE SESIÓN (Login)
                // ==========================================
                { "button.login",             "Iniciar Sesión" },
                { "button.register",          "Registrarse" },
                { "label.name",               "Usuario *" },
                { "label.password",           "Contraseña *" },
                { "checkbox.showPassword",    "Mostrar contraseña" },
                { "label.welcome",            "Bienvenido, %s" },

                // ==========================================
                // 3. BOTONES Y MENÚS PRINCIPALES
                // ==========================================
                { "button.add",               "Agregar" },
                { "button.clear",             "Limpiar" },
                { "button.update",            "Actualizar" },
                { "button.exit",              "Salir" },
                { "button.signOut",           "Cerrar Sesión" },
                { "button.view",              "Ver ▾" },
                { "button.delete",            "Eliminar ▾" },
                { "button.head",              "Ver primero" },

                { "menuItem.printSalary",     "Ver salarios" },
                { "menuItem.head",            "Ver primero" },
                { "menuItem.history",         "Ver historial" },
                { "menuItem.removeById",      "Por ID..." },
                { "menuItem.removeByPosition","Por Posición..." },
                { "menuItem.sumSalary",       "Suma de salarios" },
                { "menuItem.help",            "Ayuda" },

                // ==========================================
                // 4. COLUMNAS DE LA TABLA
                // ==========================================
                { "table.id",                 "ID" },
                { "table.name",               "Nombre" },
                { "table.salary",             "Salario" },
                { "table.x",                  "Coord. X" },
                { "table.y",                  "Coord. Y" },
                { "table.creation_date",      "Fecha de Creación" },
                { "table.position",           "Posición" },
                { "table.status",             "Estado" },
                { "table.org_name",           "Organización" },
                { "table.org_turnover",       "Facturación Anual" },
                { "table.employees",          "Nº Empleados" },
                { "table.owner",              "Propietario" },

                // ==========================================
                // 5. FORMULARIO LATERAL (Agregar/Actualizar)
                // ==========================================
                { "form.name",                "Nombre *" },
                { "form.xCoord",              "Coordenada X *" },
                { "form.yCoord",              "Coordenada Y *" },
                { "form.salary",              "Salario *" },
                { "form.position",            "Posición *" },
                { "form.status",              "Estado *" },
                { "form.orgName",             "Nombre de la Organización *" },
                { "form.orgTourn",            "Volumen de Negocio Anual *" },
                { "form.employeesCounter",    "Cantidad de Empleados *" },
                { "form.save",                "Guardar" },
                { "form.cancel",              "Cancelar" },

                // ==========================================
                // 6. MENÚ CONTEXTUAL Y VENTANAS DE DIÁLOGO
                // ==========================================
                { "menu.info",                "Ver Información" },
                { "menu.update",              "Actualizar Elemento" },
                { "menu.delete",              "Eliminar Elemento" },

                { "dialog.info.title",        "Información del Trabajador" },
                { "dialog.info.header",       "Detalles de: " },
                { "dialog.info.details",      "ID: %s\nCreador: %s\nCoordenadas: (X: %s, Y: %s)\nSalario: %s\nCargo: %s\nEstado: %s\nOrganización: %s\nFacturación Org: %s\nEmpleados Org: %s\nFecha de creación: %s" },

                { "dialog.removeId.title",    "Eliminar Trabajador" },
                { "dialog.removeId.header",   "Ingrese el ID del trabajador a eliminar:" },
                { "dialog.removePos.title",   "Eliminación Masiva" },
                { "dialog.removePos.header",  "Seleccione la posición a eliminar:" },
                { "dialog.confirmDelete.title", "Confirmar Eliminación" },
                { "dialog.confirmDelete.header","¿Estás seguro de que deseas eliminar a %s?" },

                // ==========================================
                // 7. RESPUESTAS Y MENSAJES DE CONSOLA
                // ==========================================
                { "console.action.success",   "Acción ejecutada correctamente." },
                { "console.remove.success",   "Elemento eliminado exitosamente." },
                { "console.choose.worker",    "Debes elegir un trabajador de la tabla o el mapa." },
                { "console.notResults",       "No hay datos disponibles para mostrar." },

                { "console.salary.header",    "LISTA DE SALARIOS (ORDEN DESCENDENTE):" },
                { "console.salary.empty",     "No hay salarios para mostrar." },
                { "console.sum.header",       "CÁLCULO DE NÓMINA (SUMA TOTAL DE SALARIOS):" },
                { "console.sum.total",        "Total" },

                { "console.history.header",   "HISTORIAL DE COMANDOS (Últimos 11):" },
                { "console.history.empty",    "El historial está vacío. Aún no has ejecutado comandos." },
                { "console.removeHead.success","EJECUTANDO REMOVE_HEAD...\n   [!] ELEMENTO EXTRAÍDO Y ELIMINADO:\n       > ID: %s | Nombre: %s\n       > Cargo: %s | Salario: %s\n       > Organización: %s" },
                { "console.removeHead.empty", "Operación removeHead fallida: La colección está vacía." },

                // ==========================================
                // 8. COMANDOS (Traducción del historial)
                // ==========================================
                { "cmd.add",                  "Agregar" },
                { "cmd.clear",                "Limpiar" },
                { "cmd.update",               "Actualizar" },
                { "cmd.head",                 "Ver primero (Head)" },
                { "cmd.print_salary",         "Ver salarios" },
                { "cmd.remove_by_id",         "Eliminar (Por ID)" },
                { "cmd.remove_by_pos",        "Eliminar (Por Posición)" },
                { "cmd.history",              "Ver historial" },
                { "cmd.help",                 "Ayuda" },
                { "cmd.sum_of_salary",        "Suma de salarios" },

                // ==========================================
                // 9. DESCRIPCIONES (Para el comando Help)
                // ==========================================
                { "console.help.header",      "MANUAL DE SISTEMA (COMANDOS DISPONIBLES):" },
                { "desc.add",                 "Abre el formulario lateral para registrar un nuevo elemento." },
                { "desc.clear",               "Vacía completamente la tabla y la colección." },
                { "desc.update",              "Carga los datos de la fila seleccionada para modificarlos." },
                { "desc.head",                "Muestra información detallada del primer elemento." },
                { "desc.print_salary",        "Lista en consola los salarios en orden descendente." },
                { "desc.remove_by_id",        "Pide un ID numérico y borra ese registro exacto." },
                { "desc.remove_by_pos",       "Abre un diálogo para borrar a todos por su cargo." },
                { "desc.history",             "Imprime la lista de tus últimas 11 interacciones." },
                { "desc.help",                "Muestra esta guía de comandos de la aplicación." },

                // ==========================================
                // 10. ERRORES DE VALIDACIÓN (Inputs)
                // ==========================================
                { "error.val.name",           "El nombre es obligatorio." },
                { "error.val.xCoord",         "Valor requerido: X > -497" },
                { "error.val.yCoord",         "Coordenada Y inválida." },
                { "error.val.salary",         "El salario debe ser > 0" },
                { "error.val.position",       "Seleccione un cargo." },
                { "error.val.status",         "Seleccione un estado." },
                { "error.val.orgNameLength",  "Máximo 694 caracteres." },
                { "error.val.orgNameEmpty",   "Nombre de org. obligatorio." },
                { "error.val.orgAnnual",      "El volumen debe ser > 0" },
                { "error.val.orgEmployeesCount","Empleados debe ser > 0" },
                { "error.val.numberFormat",   "Ingrese un número válido." },

                // ==========================================
                // 11. ERRORES DE BASE DE DATOS Y RED
                // ==========================================
                { "error.db.permission_denied","Acceso denegado: El elemento no te pertenece." },
                { "error.db.not_found",       "No se encontraron elementos con ese criterio para eliminar." },
                { "error.db.server_error",    "Error en el servidor de base de datos. Intente más tarde." }
        };
    }
}