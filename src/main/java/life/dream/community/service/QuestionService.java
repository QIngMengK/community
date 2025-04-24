package life.dream.community.service;

import life.dream.community.dto.QuestionDTO;
import life.dream.community.mapper.QuestionMapper;
import life.dream.community.mapper.UserMapper;
import life.dream.community.model.Question;
import life.dream.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    public List<QuestionDTO> list() {
        List<Question> questions =questionMapper.list();
        List<QuestionDTO>questionDTOList = new ArrayList<>();
        for(Question question:questions)
        {
            User user = userMapper.findbyId(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        return questionDTOList;
    }
}
