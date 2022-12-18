-- MockMvc (컨트롤러) 테스트에서만 사용
SET REFERENTIAL_INTEGRITY FALSE;
truncate table transaction_tb;
truncate table account_tb;
truncate table user_tb;
SET REFERENTIAL_INTEGRITY TRUE;