package client.i18n;

import java.util.ListResourceBundle;

public class Messages_ru_RU extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][]{

                // ==========================================
                // 1. GENERAL Y UI
                // ==========================================
                { "tab.workers",              "Работники" },
                { "tab.map",                  "Визуальная карта" },
                { "theme.light",              "☀ Светлая тема" },
                { "theme.dark",               "🌙 Темная тема" },
                { "title.console",            "Системная консоль" },
                { "status.ready",             "Система готова." },

                // ==========================================
                // 2. INICIO DE SESIÓN (Login)
                // ==========================================
                { "button.login",             "Войти" },
                { "button.register",          "Зарегистрироваться" },
                { "label.name",               "Пользователь *" },
                { "label.password",           "Пароль *" },
                { "checkbox.showPassword",    "Показать пароль" },
                { "label.welcome",            "Добро пожаловать, %s" },

                // ==========================================
                // 3. BOTONES Y MENÚS PRINCIPALES
                // ==========================================
                { "button.add",               "Добавить" },
                { "button.clear",             "Очистить" },
                { "button.update",            "Обновить" },
                { "button.exit",              "Выход" },
                { "button.signOut",           "Выйти" },
                { "button.view",              "Вид ▾" },
                { "button.delete",            "Удалить ▾" },
                { "button.head",              "Показать первый" },

                { "menuItem.printSalary",     "Показать зарплаты" },
                { "menuItem.head",            "Показать первый" },
                { "menuItem.history",         "История" },
                { "menuItem.removeById",      "По ID..." },
                { "menuItem.removeByPosition","По должности..." },
                { "menuItem.sumSalary",       "Сумма зарплат" },
                { "menuItem.help",            "Помощь" },

                // ==========================================
                // 4. COLUMNAS DE LA TABLA
                // ==========================================
                { "table.id",                 "ID" },
                { "table.name",               "Имя" },
                { "table.salary",             "Зарплата" },
                { "table.x",                  "Коорд. X" },
                { "table.y",                  "Коорд. Y" },
                { "table.creation_date",      "Дата создания" },
                { "table.position",           "Должность" },
                { "table.status",             "Статус" },
                { "table.org_name",           "Организация" },
                { "table.org_turnover",       "Годовой оборот" },
                { "table.employees",          "Кол-во сотрудников" },
                { "table.owner",              "Владелец" },

                // ==========================================
                // 5. FORMULARIO LATERAL
                // ==========================================
                { "form.name",                "Имя *" },
                { "form.xCoord",              "Коорд. X *" },
                { "form.yCoord",              "Коорд. Y *" },
                { "form.salary",              "Зарплата *" },
                { "form.position",            "Должность *" },
                { "form.status",              "Статус *" },
                { "form.orgName",             "Имя организации *" },
                { "form.orgTourn",            "Годовой оборот *" },
                { "form.employeesCounter",    "Кол-во сотрудников *" },
                { "form.save",                "Сохранить" },
                { "form.cancel",              "Отмена" },

                // ==========================================
                // 6. MENÚ CONTEXTUAL Y DIÁLOGOS
                // ==========================================
                { "menu.info",                "Просмотреть информацию" },
                { "menu.update",              "Обновить элемент" },
                { "menu.delete",              "Удалить элемент" },

                { "dialog.info.title",        "Информация о работнике" },
                { "dialog.info.header",       "Детали: " },
                { "dialog.info.details",      "ID: %s\nВладелец: %s\nКоординаты: (X: %s, Y: %s)\nЗарплата: %s\nДолжность: %s\nСтатус: %s\nОрганизация: %s\nОборот: %s\nСотрудники: %s\nДата создания: %s" },

                { "dialog.removeId.title",    "Удалить работника" },
                { "dialog.removeId.header",   "Введите ID работника для удаления:" },
                { "dialog.removePos.title",   "Массовое удаление" },
                { "dialog.removePos.header",  "Выберите должность для удаления:" },
                { "dialog.confirmDelete.title", "Подтверждение удаления" },
                { "dialog.confirmDelete.header","Вы уверены, что хотите удалить %s?" },

                // ==========================================
                // 7. MENSAJES DE CONSOLA
                // ==========================================
                { "console.action.success",   "Действие успешно выполнено." },
                { "console.remove.success",   "Элемент успешно удален." },
                { "console.choose.worker",    "Выберите работника из таблицы или карты." },
                { "console.notResults",       "Нет данных для отображения." },

                { "console.salary.header",    "СПИСОК ЗАРПЛАТ (ПО УБЫВАНИЮ):" },
                { "console.salary.empty",     "Нет зарплат для отображения." },
                { "console.sum.header",       "РАСЧЕТ ФОНДА ОПЛАТЫ ТРУДА:" },
                { "console.sum.total",        "Итого" },

                { "console.history.header",   "ИСТОРИЯ КОМАНД (Последние 11):" },
                { "console.history.empty",    "История пуста. Вы еще не выполняли команд." },
                { "console.removeHead.success","ВЫПОЛНЕНИЕ REMOVE_HEAD...\n   [!] ЭЛЕМЕНТ ИЗВЛЕЧЕН И УДАЛЕН:\n       > ID: %s | Имя: %s\n       > Должность: %s | Зарплата: %s\n       > Организация: %s" },
                { "console.removeHead.empty", "Операция removeHead не удалась: коллекция пуста." },

                // ==========================================
                // 8. COMANDOS
                // ==========================================
                { "cmd.add",                  "Добавить" },
                { "cmd.clear",                "Очистить" },
                { "cmd.update",               "Обновить" },
                { "cmd.head",                 "Показать первый (Head)" },
                { "cmd.print_salary",         "Показать зарплаты" },
                { "cmd.remove_by_id",         "Удалить (По ID)" },
                { "cmd.remove_by_pos",        "Удалить (По должности)" },
                { "cmd.history",              "История команд" },
                { "cmd.help",                 "Помощь" },
                { "cmd.sum_of_salary",        "Сумма зарплат" },

                // ==========================================
                // 9. DESCRIPCIONES (Ayuda)
                // ==========================================
                { "console.help.header",      "СИСТЕМНОЕ РУКОВОДСТВО (ДОСТУПНЫЕ КОМАНДЫ):" },
                { "desc.add",                 "Открывает боковую форму для регистрации нового элемента." },
                { "desc.clear",               "Полностью очищает таблицу и коллекцию." },
                { "desc.update",              "Загружает данные выбранной строки для редактирования." },
                { "desc.head",                "Показывает детальную информацию о первом элементе." },
                { "desc.print_salary",        "Выводит в консоль зарплаты в порядке убывания." },
                { "desc.remove_by_id",        "Запрашивает числовой ID и удаляет конкретную запись." },
                { "desc.remove_by_pos",       "Открывает диалог для удаления всех по должности." },
                { "desc.history",             "Выводит список ваших последних 11 взаимодействий." },
                { "desc.help",                "Показывает это руководство по командам приложения." },

                // ==========================================
                // 10. ERRORES DE VALIDACIÓN
                // ==========================================
                { "error.val.name",           "Имя обязательно для заполнения." },
                { "error.val.xCoord",         "Требуемое значение: X > -497" },
                { "error.val.yCoord",         "Неверная координата Y." },
                { "error.val.salary",         "Зарплата должна быть > 0" },
                { "error.val.position",       "Выберите должность." },
                { "error.val.status",         "Выберите статус." },
                { "error.val.orgNameLength",  "Максимум 694 символа." },
                { "error.val.orgNameEmpty",   "Имя организации обязательно." },
                { "error.val.orgAnnual",      "Оборот должен быть > 0" },
                { "error.val.orgEmployeesCount","Кол-во сотрудников должно быть > 0" },
                { "error.val.numberFormat",   "Введите корректное число." },

                // ==========================================
                // 11. ERRORES DE BASE DE DATOS
                // ==========================================
                { "error.db.permission_denied","Доступ запрещен: элемент вам не принадлежит." },
                { "error.db.not_found",       "Элементы по данному критерию не найдены." },
                { "error.db.server_error",    "Ошибка сервера базы данных. Попробуйте позже." }
        };
    }
}