alter table pfm_dev.recurring_transaction
    add constraint fk_recurring_transaction_user foreign key (user_no)
        references pfm_dev.user (user_no);

alter table pfm_dev.limit_expense
    add constraint fk_limit_expense_user foreign key (user_no)
        references pfm_dev.user (user_no);

alter table pfm_dev.debt
    add constraint fk_debt_user foreign key (user_no)
        references pfm_dev.user (user_no);

alter table pfm_dev.user_debt_info
    add constraint fk_user_debt_info_user foreign key (user_no)
        references pfm_dev.user (user_no);

alter table pfm_dev.notification
    add constraint fk_notification_user foreign key (user_no)
        references pfm_dev.user (user_no);