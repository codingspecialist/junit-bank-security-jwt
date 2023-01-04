package shop.mtcoding.bank.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.transaction.Transaction;
import shop.mtcoding.bank.util.CustomDateUtil;

public class AccountRespDto {
    @Setter
    @Getter
    public static class AccountSaveRespDto {
        private Long id;
        private Long number;
        private Long balance;

        public AccountSaveRespDto(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }

    // 입금은 인증이 안되어 있어서 잔액을 확인할 수 없다
    @Setter
    @Getter
    public static class AccountDepositRespDto {
        private Long id; // 계좌 ID
        private Long number; // 계좌번호
        private TransactionDto transaction; // 거래내역

        public AccountDepositRespDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.transaction = new TransactionDto(transaction);
        }

        @Getter
        @Setter
        public class TransactionDto {
            private Long id;
            private String gubun; // 입금
            private String sender; // ATM
            private String reciver;
            private Long amount;
            @JsonIgnore
            private Long depositAccountBalance;
            private String tel;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.reciver = transaction.getReciver();
                this.amount = transaction.getAmount();
                this.amount = transaction.getDepositAccountBalance();
                this.tel = transaction.getTel();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    @Setter
    @Getter
    public static class AccountWithdrawRespDto {
        private Long id; // 계좌 ID
        private Long number; // 계좌번호
        private Long balance; // 잔액
        private TransactionDto transaction; // 거래내역

        public AccountWithdrawRespDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transaction = new TransactionDto(transaction);
        }

        @Getter
        @Setter
        public class TransactionDto {
            private Long id;
            private String gubun; // 출금
            private String sender; // 출금 계좌번호
            private String reciver; // ATM
            private Long amount;
            private Long withdrawAccountBalance;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.reciver = transaction.getReciver();
                this.amount = transaction.getAmount();
                this.withdrawAccountBalance = transaction.getWithdrawAccountBalance();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

    @Setter
    @Getter
    public static class AccountTransferRespDto {
        private Long id; // 계좌 ID
        private Long number; // 계좌번호
        private Long balance; // 잔액
        private TransactionDto transaction; // 거래내역

        public AccountTransferRespDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transaction = new TransactionDto(transaction);
        }

        @Getter
        @Setter
        public class TransactionDto {
            private Long id;
            private String gubun; // 출금
            private String sender; // 출금 계좌번호
            private String reciver; // ATM
            private Long amount;
            private Long withdrawAccountBalance;
            @JsonIgnore
            private Long depositAccountBalance;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.reciver = transaction.getReciver();
                this.amount = transaction.getAmount();
                this.withdrawAccountBalance = transaction.getWithdrawAccountBalance();
                this.depositAccountBalance = transaction.getDepositAccountBalance();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }
}
