package shop.mtcoding.bank.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.transaction.Transaction;
import shop.mtcoding.bank.domain.transaction.TransactionQueryRepository;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.transaction.TransactionRespDto.TransactionListRespDto;
import shop.mtcoding.bank.handler.ex.CustomApiException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TransactionService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final AccountRepository accountRepository;
    private final TransactionQueryRepository transactionQueryRepository;

    // 계좌상세보기할 때 전체 계좌목록 나옴
    // 입금만 보기, 출금만 보기, 전체보기할 때 Account, User 정보 없이 순수 Transaction 내용만 불러올때 동적쿼리 사용
    @Transactional
    public TransactionListRespDto 입출금목록보기(Long userId, Long accountNumber, String gubun, Integer page) {
        // 계좌 확인
        Account accountPS = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new CustomApiException(""));

        // 계좌 소유자 확인
        accountPS.checkOwner(userId);

        // 입출금 내역 조회
        List<Transaction> transactionListPS = transactionQueryRepository.findByAccountId(accountPS.getId(), gubun,
                page);

        // DTO (동적 쿼리)
        return new TransactionListRespDto(transactionListPS, accountNumber);
    }

}
