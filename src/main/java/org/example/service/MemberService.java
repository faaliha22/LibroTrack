package org.example.service;

import org.example.entity.Member;
import org.example.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member addMember(Member member) {

        String name = member.getName().trim();
        String email = member.getEmail().trim().toLowerCase();
        String phone = member.getPhone().trim();

        // Duplicate email check
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "A member with this email already exists."
            );
        }

        // Duplicate phone check
        if (memberRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException(
                    "A member with this phone number already exists."
            );
        }

        member.setName(name);
        member.setEmail(email);
        member.setPhone(phone);

        return memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}