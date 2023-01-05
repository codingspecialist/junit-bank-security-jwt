package shop.mtcoding.bank.dto.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.transaction.Transaction;
import shop.mtcoding.bank.util.CustomDateUtil;

public class TransactionRespDto {
    @Getter
    @Setter
    public static class TransactionListRespDto {
        private List<TransactionDto> transactions = new ArrayList<>();

        public TransactionListRespDto(List<Transaction> transactions, Long accountNumber) {
            this.transactions = transactions.stream()
                    .map((transaction) -> new TransactionDto(transaction, accountNumber)).collect(Collectors.toList());
        }

        @Getter
        @Setter
        public class TransactionDto {

            private Long id;
            private String gubun;
            private Long amount;

            private String sender;
            private String reciver;

            private String tel;
            private String createdAt;
            private Long balance;

            public TransactionDto(Transaction transaction, Long accountNumber) {
                System.out.println("디버그 : accountNumber : " + accountNumber);
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.amount = transaction.getAmount();
                this.sender = transaction.getSender();
                this.reciver = transaction.getReciver();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
                this.tel = transaction.getTel() == null ? "없음" : transaction.getTel();

                if (transaction.getDepositAccount() == null) {
                    this.balance = transaction.getWithdrawAccountBalance();
                } else if (transaction.getWithdrawAccount() == null) {
                    this.balance = transaction.getDepositAccountBalance();
                } else {
                    if (accountNumber.longValue() == transaction.getDepositAccount().getNumber().longValue()) {
                        this.balance = transaction.getDepositAccountBalance();
                    } else {
                        this.balance = transaction.getWithdrawAccountBalance();
                    }
                }

            }
        }
    }
}
