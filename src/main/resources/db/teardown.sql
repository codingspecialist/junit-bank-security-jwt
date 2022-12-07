-- MockMvc (컨트롤러) 테스트에서만 사용
SET REFERENTIAL_INTEGRITY FALSE;
truncate table transaction;
truncate table account;
truncate table users;
SET REFERENTIAL_INTEGRITY TRUE;