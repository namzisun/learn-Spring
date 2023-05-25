package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)  // 이 서비스 안에는 읽기 전용이 많으니까 readOnly
@RequiredArgsConstructor
public class MemberService {

// 방법 1 - 생성자로 인젝션(방법 3의 업그레이드)
    private final MemberRepository memberRepository;

// 방법 2 - spring이 주입해주는게 아닌 방식, 중간에 주입을 해줌
// 장점 : 테스트시에 목업을 주입해줄 수도 있음
// 단점 : 중간에 레포를 바꿔버릴 수도 있으니 취약. 어차피 바뀌지 않을 존재인데 걍 처음부터 주입해버리는게 나음. 굳이 나중에 주입할 필요가 없는 것
//    private MemberRepository memberRepository;
//
//    @Autowired
//    public setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }


// 방법 3 - 생성자로 주입. final을 써서 무조건 생성시에 주입하도록 해줌
//    private final MemberRepository memberRepository;
//
//    @Autowired
//    public MemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입
     */
    @Transactional  // 쓰기 전용이 됨(readOnly가 꺼짐)
    public Long join(Member member) {
        validateDuplicateMember(member);    // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
