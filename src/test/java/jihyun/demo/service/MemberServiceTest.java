package jihyun.demo.service;

import jihyun.demo.domain.Member;
import jihyun.demo.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

// 순수 자바 기반의 테스트
class MemberServiceTest {

    // MemoryService에서 사용하는 MemoryMemberRepository와 별도의 객체
    // MemoryMemberRepository memberRepository = new MemoryMemberRepository();
    // MemberService memberService = new MemberService();

    MemoryMemberRepository memberRepository;
    MemberService memberService;

    // 테스트 간에 서로 영향이 없도록 매번 새로운 객체 생성
    @BeforeEach
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        // Service와 테스트에서 같은 repository 사용
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void join() {
        // given
        Member member = new Member();
        member.setName("kim");

        // when
        Long saveId = memberService.join(member);

        // then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(findMember.getName()).isEqualTo(member.getName());
    }

    // 예외 테스트
    @Test
    void joinDuplicate() {
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member1);
        // 예외가 발생하지 않거나, 종류가 다르면 fail
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        // then
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
/*
        try {
            memberService.join(member2);
            fail();
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        }
*/
    }
}