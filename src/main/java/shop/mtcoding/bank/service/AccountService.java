package shop.mtcoding.bank.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.transaction.Transaction;
import shop.mtcoding.bank.domain.transaction.TransactionEnum;
import shop.mtcoding.bank.domain.transaction.TransactionRepository;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.account.AccountReqDto.AccountSaveReqDto;
import shop.mtcoding.bank.dto.account.AccountRespDto.AccountSaveRespDto;
import shop.mtcoding.bank.handler.ex.CustomApiException;
import shop.mtcoding.bank.util.CustomDateUtil;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public AccountSaveRespDto 계좌등록(AccountSaveReqDto accountSaveReqDto, Long userId) {
        User userPS = userRepository.findById(userId).orElseThrow(
                () -> new CustomApiException("유저를 찾을 수 없습니다"));
        Account accountPS = accountRepository.save(accountSaveReqDto.toEntity(userPS));
        return new AccountSaveRespDto(accountPS);
    }

    // 삭제는 보통 put요청으로 update를 하고, 상태변경을 한다. 계좌 활성화 상태!!
    // put 요청을 해야 body 값을 받을 수 있고 password 검증이 가능해진다. (숙제)
    @Transactional
    public void 계좌삭제(Long accountNumber, Long userId) {
        // 1. 계좌 확인
        Account accountPS = accountRepository.findByNumber(accountNumber).orElseThrow(
                () -> new CustomApiException("계좌를 찾을 수 없습니다"));

        // 2. 계좌 소유자 확인
        accountPS.checkOwner(userId);

        // 3. 계좌 삭제
        accountRepository.deleteById(accountPS.getId());
    }

    @Transactional
    public AccountDepositRespDto 계좌입금(AccountDepositReqDto accountDepositReqDto) {
        // GUBUN값 검증
        if (!accountDepositReqDto.getGubun().equals("DEPOSIT")) {
            throw new CustomApiException("구분값 검증 실패");
        }

        // 입금계좌 확인
        Account depositAccountPS = accountRepository.findByNumber(accountDepositReqDto.getDepositAccountNumber())
                .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다"));

        // 0원 체크
        depositAccountPS.checkZeroAmount(accountDepositReqDto.getAmount());

        // 입금 하기
        depositAccountPS.deposit(accountDepositReqDto.getAmount());

        // 거래내역 남기기
        Transaction transaction = Transaction.builder()
                .depositAccount(depositAccountPS)
                .withdrawAccount(null)
                .depositAccountBalance(depositAccountPS.getBalance())
                .withdrawAccountBalance(null)
                .amount(accountDepositReqDto.getAmount())
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .reciver(depositAccountPS.getNumber() + "")
                .tel(accountDepositReqDto.getTel())
                .build();
        Transaction transactionPS = transactionRepository.save(transaction);

        // DTO
        return new AccountDepositRespDto(depositAccountPS, transactionPS);
    }

    @Getter
    @Setter
    public static class AccountDepositReqDto {
        private Long depositAccountNumber;
        private Long amount;
        private String gubun;
        private String tel;
    }

    @Setter
    @Getter
    public static class AccountDepositRespDto {
        private Long id; // 계좌 ID
        private Long number; // 계좌번호
        private Long balance; // 잔액
        private TransactionDto transaction; // 거래내역

        public AccountDepositRespDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transaction = new TransactionDto(transaction);
        }

        @Getter
        @Setter
        public class TransactionDto {
            private Long id;
            private Long amount;
            private String gubun; // 입금
            private String sender; // ATM
            private String reciver;
            private String tel;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.amount = transaction.getAmount();
                this.gubun = transaction.getGubun().getValue();
                this.sender = transaction.getSender();
                this.reciver = transaction.getReciver();
                this.tel = transaction.getTel();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }

}
