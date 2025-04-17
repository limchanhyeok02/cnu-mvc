package cnu.mvc.domain.member;

import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MemberRepository {

    private static final Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    // 회원 저장: ID 부여 후 Map에 저장
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    // ID로 회원 조회
    public Member findById(Long id) {
        return store.get(id);
    }

    // 이메일로 회원 조회 (회원가입 중복검사 및 로그인 시 사용)
    public Member findByEmail(String email) {
        for (Member m : store.values()) {
            if (m.getEmail().equals(email)) {
                return m;
            }
        }
        return null;
    }
}
