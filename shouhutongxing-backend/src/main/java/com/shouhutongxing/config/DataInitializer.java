package com.shouhutongxing.config;

import com.shouhutongxing.entity.Student;
import com.shouhutongxing.entity.MaterialDemand;
import com.shouhutongxing.repository.StudentRepository;
import com.shouhutongxing.repository.MaterialDemandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 数据初始化器
 *
 * @author washingtonwood
 * @since 2025-03-11
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final MaterialDemandRepository demandRepository;

    @Override
    public void run(String... args) {
        // 检查是否需要初始化测试数据
        if (studentRepository.count() == 0) {
            log.info("数据库为空，开始初始化测试数据...");
            initStudentData();
            initMaterialDemandData();
            log.info("测试数据初始化完成！共插入 {} 条学生记录，{} 条物资需求记录",
                studentRepository.count(), demandRepository.count());
        } else {
            log.info("数据库已有 {} 条学生记录，跳过初始化", studentRepository.count());
        }
    }

    /**
     * 初始化学生测试数据
     */
    private void initStudentData() {
        studentRepository.saveAll(java.util.List.of(
            createStudent("2025001", "小明", "男", 5, "5年1班", 92.00,
                    "父母在外务工，跟随年迈的祖父母生活", "急需学习用品和冬季衣物"),
            createStudent("2025002", "小红", "女", 4, "4年2班", 88.00,
                    "单亲家庭，母亲患病丧失劳动能力", "急需课外读物和文具"),
            createStudent("2025003", "小强", "男", 6, "6年1班", 85.00,
                    "家里有三个孩子上学，经济压力大", "需要辅导资料和体育用品"),
            createStudent("2025004", "小丽", "女", 3, "3年1班", 80.00,
                    "留守儿童，父母常年在外打工", "需要绘画用品和益智玩具"),
            createStudent("2025005", "小华", "男", 5, "5年2班", 78.00,
                    "家庭务农为主要收入，收入不稳定", "需要课外读物和书包"),
            createStudent("2025006", "小芳", "女", 4, "4年1班", 75.00,
                    "家庭人口多，负担重", "需要文具和美术用品"),
            createStudent("2025007", "小军", "男", 6, "6年2班", 72.00,
                    "父亲因病丧失劳动能力", "急需科普读物和学习用品"),
            createStudent("2025008", "小梅", "女", 3, "3年2班", 70.00,
                    "偏远山区，交通不便", "需要保暖衣物和文具"),
            createStudent("2025009", "小伟", "男", 5, "5年3班", 68.00,
                    "经济来源单一", "需要体育用品和课外读物"),
            createStudent("2025010", "小娜", "女", 4, "4年3班", 65.00,
                    "家庭经济困难，缺乏基本学习用品", "急需全套文具和书籍"),
            createStudent("2025011", "小东", "男", 6, "6年3班", 62.00,
                    "父母收入低，供养多个子女", "需要辅导资料和工具书"),
            createStudent("2025012", "小静", "女", 3, "3年3班", 60.00,
                    "一般困难家庭", "需要美术用品和绘本")
        ));
    }

    /**
     * 初始化物资需求数据
     */
    private void initMaterialDemandData() {
        // 为每个学生添加物资需求
        int demandId = 1;

        // 小明的需求
        createDemand(1L, "学习用品", "文具", "基础文具套装", "铅笔、橡皮、尺子、练习本等基础文具", 50, 5, "URGENT");
        createDemand(1L, "衣物", "冬装", "保暖羽绒服", "冬季御寒衣物，尺码140cm", 2, 0, "URGENT");

        // 小红的需求
        createDemand(2L, "书籍", "故事书", "儿童文学读物", "适合小学生阅读的故事书、童话书", 30, 8, "URGENT");
        createDemand(2L, "学习用品", "文具", "高档文具套装", "钢笔、彩笔、素描本等美术文具", 20, 3, "NORMAL");

        // 小强的需求
        createDemand(3L, "书籍", "辅导材料", "各科辅导资料", "语文、数学、英语等科目辅导书", 15, 2, "NORMAL");
        createDemand(3L, "体育用品", "球类", "篮球", "标准5号篮球，用于体育锻炼", 3, 1, "NORMAL");

        // 小丽的需求
        createDemand(4L, "学习用品", "文具", "绘画用品套装", "蜡笔、水彩笔、画纸等绘画工具", 25, 0, "NORMAL");
        createDemand(4L, "其他物资", "玩具", "益智拼图", "培养思维能力的拼图玩具", 10, 2, "NORMAL");

        // 小华的需求
        createDemand(5L, "书籍", "课外读物", "科普读物", "自然科学、百科全书等科普书籍", 20, 5, "NORMAL");
        createDemand(5L, "学习用品", "书包", "双肩书包", "防水耐用的学生书包", 2, 0, "NORMAL");

        // 小芳的需求
        createDemand(6L, "学习用品", "文具", "美术用品套装", "彩色铅笔、素描纸、调色盘等", 30, 8, "NORMAL");
        createDemand(6L, "学习用品", "本册", "各类练习本", "田字格、算术本、英语本等", 100, 20, "NORMAL");

        // 小军的需求（紧急）
        createDemand(7L, "书籍", "科普书", "少儿科普百科", "适合小学生的科普读物", 25, 3, "URGENT");
        createDemand(7L, "学习用品", "文具", "学习用品大礼包", "包含各类文具的综合套装", 15, 1, "URGENT");

        // 小梅的需求
        createDemand(8L, "衣物", "秋冬装", "保暖衣物套装", "毛衣、棉裤、外套等", 5, 0, "URGENT");

        // 小伟的需求
        createDemand(9L, "体育用品", "运动器材", "羽毛球拍", "羽毛球运动器材", 4, 1, "NORMAL");

        // 小娜的需求（紧急）
        createDemand(10L, "学习用品", "文具", "全套文具礼盒", "包含铅笔、钢笔、尺子、橡皮、卷笔刀等", 10, 0, "URGENT");
        createDemand(10L, "书籍", "绘本", "儿童绘本套装", "精美绘本，培养阅读兴趣", 15, 2, "URGENT");

        // 小东的需求
        createDemand(11L, "书籍", "工具书", "学生工具书", "字典、词典、手册等工具书", 8, 1, "NORMAL");

        // 小静的需求
        createDemand(12L, "学习用品", "美术用品", "儿童绘画套装", "水彩笔、蜡笔、画板等", 20, 6, "NORMAL");
    }

    /**
     * 创建物资需求
     */
    private void createDemand(Long studentId, String category, String categoryDetail,
                               String name, String description, Integer requiredQuantity,
                               Integer receivedQuantity, String urgencyLevel) {
        MaterialDemand demand = new MaterialDemand();
        demand.setChildId(studentId);
        demand.setCategory(category);
        demand.setCategoryDetail(categoryDetail);
        demand.setName(name);
        demand.setDescription(description);
        demand.setRequiredQuantity(requiredQuantity);
        demand.setReceivedQuantity(receivedQuantity);
        demand.setUrgencyLevel(MaterialDemand.UrgencyLevel.valueOf(urgencyLevel));
        demand.setStatus(MaterialDemand.DemandStatus.ACTIVE);
        demand.setCreatedTime(java.time.LocalDateTime.now());
        demand.setUpdatedTime(java.time.LocalDateTime.now());

        demandRepository.save(demand);
        log.debug("创建物资需求: 学生ID={}, 分类={}, 名称={}", studentId, category, name);
    }

    private Student createStudent(String sn, String name, String gender, Integer year,
                                   String clazz, Double weight, String familyCondition, String urgentNeed) {
        Student student = new Student();
        student.setSn(sn);
        student.setName(name);
        student.setGender(gender);
        student.setYear(year);
        student.setClazz(clazz);
        student.setRecommendationWeight(new BigDecimal(weight));
        student.setPhoto("/uploads/students/photo" + Math.abs(name.hashCode()) % 6 + 1 + ".jpg");
        student.setFamilyCondition(familyCondition);
        student.setUrgentNeed(urgentNeed);
        return student;
    }
}
