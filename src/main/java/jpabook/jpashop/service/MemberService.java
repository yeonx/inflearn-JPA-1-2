package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor //final이 있는 것들만 가지고 생성자를 만들어줌
public class MemberService {


    private final MemberRepository memberRepository; //final 넣으면 값 세팅여부 확인 가능
//
//    @Autowired //생성자가 하나일 경우엔 자동으로 인젝션해줘서 이거 안써도 됨
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }


    //회원 가입
    @Transactional //이 곳에 transactional이 먼저여서 이 곳의 readOnly가 false임
    public Long join(Member member){
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {

        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
//    @Transactional(readOnly = true) // 조회하는 곳에선 성능을 좋게 해주는 효과가 있음, 쓰기에선 안됨
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //단건조회회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
