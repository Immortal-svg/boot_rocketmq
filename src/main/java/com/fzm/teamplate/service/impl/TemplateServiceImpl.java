package com.fzm.teamplate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzm.teamplate.mapper.TemplateMapper;
import com.fzm.teamplate.po.Template;
import com.fzm.teamplate.service.TemplateService;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @date 2020/2/24
 */
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements TemplateService {
}
