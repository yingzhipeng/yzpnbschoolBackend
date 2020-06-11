package com.yzpnb.ucenter_service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yzpnb.utils.JwtUtils;
import com.yzpnb.common_utils.MD5;
import com.yzpnb.service_base_handler.CustomExceptionHandler;
import com.yzpnb.ucenter_service.entity.UcenterMember;
import com.yzpnb.ucenter_service.entity.vo.RegisterVo;
import com.yzpnb.ucenter_service.mapper.UcenterMemberMapper;
import com.yzpnb.ucenter_service.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-06-03
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    /**
     * 登录，判断登录的手机号和密码是否正确，返回JWT加密后token字符串
     * @param ucenterMember
     * @return
     */
    @Override
    public String login(UcenterMember ucenterMember) {
        /**判断手机号和密码是否为空，为空直接终止程序，返回自定义异常信息*/
        if(StringUtils.isEmpty(ucenterMember.getMobile()) || StringUtils.isEmpty(ucenterMember.getPassword())){
            throw new CustomExceptionHandler(20001,"手机号或密码为空");
        }

        /**判断手机号和密码是否正确，账号是否可用，有一项出错，直接返回自定义异常**/
        //根据手机号查询数据，有值表示手机号正确，没值直接失败
        QueryWrapper<UcenterMember> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("mobile",ucenterMember.getMobile());
        UcenterMember one = baseMapper.selectOne(queryWrapper);

        if(one !=null){//有值表示手机号正确
            //通过MD5加密用户输入的密码，比较是否与数据库中密码相同
            String password= MD5.encrypt(ucenterMember.getPassword());//根据工具类获取加密后密码
            if(!one.getPassword().equals(password)){//判断密码是否正确,不正确返回false，通过非运算获取到true，报异常
                throw new CustomExceptionHandler(20001,"密码错误");
            }
            //判断当前是否禁用状态
            if(one.getIsDisabled()){//如果isDisable为真值，表示现在此账号是禁用状态
                throw new CustomExceptionHandler(20001,"此账号现在被禁用，可能有其他人登录");
            }

        }else{//没有值表示手机号没有注册
            throw new CustomExceptionHandler(20001,"此手机号没有注册");
        }

        /**走到这说明信息全部正确，使用JWT根据id和昵称生成token字符串并返回**/
        String token = JwtUtils.getJwtToken(one.getId(), one.getNickname());//传入我们查出来的对象的值，形参中，只有电话号和密码

        return token;
    }

    /**
     * 注册，判断注册手机号是否已经存在，密码需要加密存储，需要验证码正确才能注册成功
     * @param registerVo
     */
    @Autowired
    RedisTemplate<String,String> redisTemplate=new RedisTemplate<>();//创建redisTemplate对象，使用redis数据库
    @Override
    public void register(RegisterVo registerVo) {
        /**获取数据**/
        String mobile=registerVo.getMobile();     //获取手机号
        String password=registerVo.getPassword(); //获取密码
        String nickname=registerVo.getNickname(); //获取昵称
        String code=registerVo.getCode();         //获取验证码

        /**判断手机号和密码是否为空，为空直接终止程序，返回自定义异常信息，不为空,判断手机号是否重复*/
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new CustomExceptionHandler(20001,"手机号或密码为空");
        }else{
            //不为空则判断手机号是否重复
            QueryWrapper<UcenterMember> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("mobile",mobile);
            int count = baseMapper.selectCount(queryWrapper);//查询是否有相同电话号，返回符合匹配条件的数据条数

            if(count>0){//如果满足条件的个数>0表示已经有此电话号，抛异常
                throw new CustomExceptionHandler(20001,"此手机号已经被注册");
            }
        }

        /**判断昵称是否为空，不为空判断是否重复,如果昵称为空，或者昵称重复，报自定义异常**/
        if(StringUtils.isEmpty(nickname)){
            throw new CustomExceptionHandler(20001,"请输入昵称");
        }else{
            //不为空判断是否昵称重复
            QueryWrapper<UcenterMember> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("nickname",nickname);
            int count = baseMapper.selectCount(queryWrapper);//查询是否有相同电话号，返回符合匹配条件的数据条数

            if(count>0){//如果满足条件的个数>0表示已经有此昵称，抛异常
                throw new CustomExceptionHandler(20001,"昵称重复");
            }

        }

        /**判断当前手机号的验证码是否已经过期，或者没有生成**/
        if(redisTemplate.hasKey(mobile)==false) throw new CustomExceptionHandler(20001,"此手机号的验证码没有生成或已过期");

        /**确保发送过验证码以后，判断验证码是否为空,不为空则判断验证码是否正确，为空，报自定义异常**/
        if(StringUtils.isEmpty(code)){
            throw new CustomExceptionHandler(20001,"请输入验证码");
        }else{
            //判断验证码是否与redis中存储的key为当前手机号的value值相等
            String value = redisTemplate.opsForValue().get(mobile);//获取key为当前手机号的value值
            if(!value.equals(code)){
                throw new CustomExceptionHandler(20001,"验证码错误");
            }
        }

        /**走到这说明没问题，将密码加密，存入数据库**/
        //一切信息正确且不重复，加密密码
        password=MD5.encrypt(password);

        UcenterMember ucenterMember=new UcenterMember();//创建用户对象
        ucenterMember.setMobile(mobile);
        ucenterMember.setPassword(password);
        ucenterMember.setNickname(nickname);
        ucenterMember.setIsDisabled(false);//用户不禁用
        //设置默认头像
        ucenterMember.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");

        baseMapper.insert(ucenterMember);//添加数据
    }

    /**
     * 根据年-月-日格式字符串，查询指定日期的注册人数
     * @param day
     * @return
     */
    @Override
    public Integer countRegisterByDay(String day) {

        return baseMapper.countRegisterByDay(day);
    }
}
