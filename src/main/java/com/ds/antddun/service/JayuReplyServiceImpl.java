package com.ds.antddun.service;

import com.ds.antddun.dto.JayuReplyDTO;
import com.ds.antddun.entity.JayuReply;
import com.ds.antddun.repository.JayuReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JayuReplyServiceImpl implements JayuReplyService{

    private final JayuReplyRepository jayuReplyRepository;

    @Override
    public List<JayuReply> getReplyByJayuNo(Long jayuNo) {

        List<JayuReply> result = jayuReplyRepository.getReplyListByJayuNo(jayuNo);

        return  result;
    }

    @Override
    public Long register(JayuReplyDTO jayuReplyDTO) {
        JayuReply jayuReply = dtoToEntity(jayuReplyDTO);
        jayuReplyRepository.save(jayuReply);

        return jayuReply.getJayuRno();
    }

    @Transactional
    @Override
    public void modify(JayuReplyDTO jayuReplyDTO) {
        Optional<JayuReply> result = jayuReplyRepository.findById(jayuReplyDTO.getJayuRno());

        if (result.isPresent()) {
            JayuReply jayuReply = result.get();
            jayuReply.setReplyText(jayuReplyDTO.getReplyText());
            jayuReplyRepository.save(jayuReply);
        }
    }

    @Transactional
    @Override
    public void replyRemove(Long jayuRno) {
        jayuReplyRepository.deleteById(jayuRno);
    }

    @Transactional
    @Override
    public void remove(Long jayuNo){
        jayuReplyRepository.remove(jayuNo);
    }
}
