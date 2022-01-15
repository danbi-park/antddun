package com.ds.antddun.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "jayuBoard")
@SequenceGenerator(name = "JAYUREPLY_SEQ_GEN", sequenceName = "JAYUREPLY_SEQ", initialValue = 1, allocationSize = 1)
public class JayuReply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JAYUREPLY_SEQ")
    private Long jayuRno;

    private String replyText;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private JayuBoard jayuBoard;
}
