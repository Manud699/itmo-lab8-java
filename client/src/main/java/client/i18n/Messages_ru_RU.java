package client.i18n;

import java.util.ListResourceBundle;

public class Messages_ru_RU extends ListResourceBundle {

    @Override
    protected Object[][] getContents(){

        return new Object[][] {
                // --- BOTONES (КНОПКИ) ---
                { "button.add", "Добавить" },
                { "button.clear", "Очистить" },
                { "button.update", "Обновить" },
                { "button.exit", "Выход" },
                { "button.signOut", "Выйти из системы" },
                { "button.view", "Просмотр ▾"},
                { "menuItem.head", "Показать первый" },
                { "menuItem.printSalary", "Зарплаты (По убыванию)"},
                { "button.delete", "Удалить ▾" },
                { "menuItem.removeById", "По ID..." },
                { "menuItem.removeByPosition", "По должности..." },
                { "menuItem.history", "История команд" },
                { "menuItem.help", "Помощь" },

                { "cmd.add", "Добавить" },
                { "cmd.clear", "Очистить" },
                { "cmd.update", "Обновить" },
                { "cmd.head", "Первый элемент" },
                { "cmd.print_salary", "Зарплаты (По убыванию)" },
                { "cmd.remove_by_id", "Удалить (По ID)" },
                { "cmd.remove_by_pos", "Удалить (По должности)" },
                { "cmd.history", "История команд" },
                { "cmd.help", "Помощь" },


                // --- TAB (ВКЛАДКИ) ---
                { "tab.workers", "Работники" },

                // --- LOGIN (АВТОРИЗАЦИЯ) ---
                { "button.login", "Войти" },
                { "button.register", "Зарегистрироваться" },
                { "label.password", "Пароль *" },
                { "label.name", "Имя пользователя *" },

                // --- COLUMNAS DE LA TABLA (СТОЛБЦЫ ТАБЛИЦЫ) ---
                { "table.id", "ID" },
                { "table.name", "Имя" },
                { "table.salary", "Зарплата" },
                { "table.x", "Коорд. X" },
                { "table.y", "Коорд. Y" },
                { "table.creation_date", "Дата создания" },
                { "table.position", "Должность" },
                { "table.status", "Статус" },
                { "table.org_name", "Организация" },
                { "table.org_turnover", "Годовой оборот" },
                { "table.employees", "Кол-во сотрудников" },
                { "table.owner", "Владелец" },

                // --- MENSAJES DE ESTADO / CONSOLA (СООБЩЕНИЯ СИСТЕМЫ) ---
                { "status.ready", "Система готова." },

                // --- ERRORES DE VALIDACIÓN (ОШИБКИ ВАЛИДАЦИИ) ---
                { "error.val.name", "Имя обязательно." },
                { "error.val.xCoord", "Требуется значение: X > -497" },
                { "error.val.xCoord", "Требуется значение: X > -497" },
                { "error.val.yCoord", "Неверная координата Y." },
                { "error.val.salary", "Зарплата должна быть > 0" },
                { "error.val.position", "Выберите должность." },
                { "error.val.status", "Выберите статус." },
                { "error.val.orgNameLength", "Максимум 694 символа." },
                { "error.val.orgNameEmpty", "Название орг. обязательно." },
                { "error.val.orgAnnual", "Оборот должен быть > 0" },
                { "error.val.orgEmployeesCount", "Сотрудников должно быть > 0" },
                { "error.val.numberFormat", "Введите корректное число." },

                // --- ОШИБКИ БАЗЫ ДАННЫХ И СЕТИ ---
                { "error.db.permission_denied", "Доступ запрещен: Элемент не существует или не принадлежит вам." },
                { "error.db.not_found", "Не найдено элементов по данному критерию для удаления." },
                { "error.db.server_error", "Ошибка сервера базы данных. Попробуйте позже." },

                // --- FORM WORKER (ФОРМА РАБОТНИКА) ---
                { "form.name", "Имя *" },
                { "form.xCoord", "Координата X *" },
                { "form.yCoord", "Координата Y *" },
                { "form.salary", "Зарплата *" },
                { "form.position", "Должность *" },
                { "form.status", "Статус *" },
                { "form.orgName", "Название организации *" },
                { "form.orgTourn", "Годовой оборот *" },
                { "form.employeesCounter", "Кол-во сотрудников *" },
                { "form.save", "Сохранить" },
                { "form.cancel", "Отмена" },

                // --- MENU CONTEXTUAL Y DIALOGO (КОНТЕКСТНОЕ МЕНЮ И ДИАЛОГ) ---
                { "menu.delete", "Удалить элемент" },
                { "menu.info", "Посмотреть информацию" },
                { "menu.update", "Обновить элемент" },

                { "dialog.info.title", "Информация о работнике" },
                { "dialog.info.header", "Детали:" },
                { "dialog.info.details", "ID: %s\nСоздатель: %s\nКоординаты: (X: %s, Y: %s)\nЗарплата: %s\nДолжность: %s\nСтатус: %s\nОрганизация: %s\nОборот орг: %s\nСотрудники орг: %s\nДата создания: %s" },
                { "dialog.removeId.title", "Удалить работника" },
                { "dialog.removeId.header", "Введите ID работника для удаления:" },
                { "dialog.removePos.title", "Массовое удаление" },
                { "dialog.removePos.header", "Выберите должность для удаления:" },
                { "dialog.confirmDelete.title", "Подтверждение удаления" },
                { "dialog.confirmDelete.header", "Вы уверены, что хотите удалить: %s?" },

                // --- CONSOLA DE COMANDOS (КОНСОЛЬ КОМАНД) ---
                { "console.removeHead.empty", "Ошибка removeHead: Коллекция пуста." },
                { "console.removeHead.success", "ВЫПОЛНЕНИЕ REMOVE_HEAD...\n   [!] ЭЛЕМЕНТ ИЗВЛЕЧЕН И УДАЛЕН:\n       > ID: %s | Имя: %s\n       > Должность: %s | Зарплата: %s\n       > Организация: %s" },
                { "console.salary.header", "СПИСОК ЗАРПЛАТ (ПО УБЫВАНИЮ):\n" },
                { "console.salary.empty", "Нет зарплат для отображения." },
                { "console.notResults", "Нет данных для отображения." },
                { "console.remove.success", "Команда выполнена. Элементы удалены или не найдены." },
                { "console.chose.worker", "Вы должны выбрать работника"},
                { "console.action.success", "Правильно выполненное действие"},

                { "console.help.header", "СИСТЕМНОЕ РУКОВОДСТВО (ДОСТУПНЫЕ КОМАНДЫ):" },
                { "desc.add", "Открывает боковую форму для регистрации нового элемента." },
                { "desc.clear", "Полностью очищает таблицу и коллекцию." },
                { "desc.update", "Загружает данные выбранной строки для редактирования." },
                { "desc.head", "Показывает подробную информацию о первом элементе." },
                { "desc.print_salary", "Выводит в консоль зарплаты в порядке убывания." },
                { "desc.remove_by_id", "Запрашивает числовой ID и удаляет эту запись." },
                { "desc.remove_by_pos", "Открывает диалог для массового удаления по должности." },
                { "desc.history", "Печатает список ваших последних 11 взаимодействий." },
                { "desc.help", "Показывает это руководство по командам приложения." },

                // --- HISTORIAL DE CONSOLA ---
                { "console.history.header", "ИСТОРИЯ КОМАНД (Последние 11):" },
                { "console.history.empty", "История пуста. Вы еще не выполняли команд." }

        };
    }
}
