package com.ds.antddun.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"jayuBoard", "member"})
@Table(name="jayu_likes")
public class JayuLikes {
    //일련번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jayuLno;

    //게시물 번호
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "jayuNo")
    private JayuBoard jayuBoard;

    //회원 번호
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "mno")
    private Member member;

    public JayuLikes(Long jayuLno, JayuBoard jayuBoard, Member member) {
        this.jayuLno = jayuLno;
        this.jayuBoard = jayuBoard;
        this.member = member;
    }

}
