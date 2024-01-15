package org.feuyeux.io;

import io.fury.Fury;
import io.fury.config.FuryBuilder;
import io.fury.config.Language;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class DTOTest {
    @Data
    public static class BaseDTO {

        private int pid;

        private String cateName;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class UserDTO extends BaseDTO implements Serializable {
        private Long userId;
        private OrderDTO[] orderDTOS;
        private Integer[] nums;
        private int[] nums2;
        private ColorEnum colorEnum;
        List<OrderDTO> orderDTOList;
        private Byte propByte;
        private Short propShort;
        private Integer propInteger;
        private Float propFloat;
        private Double propDouble;
        private int propinnt;
        private float propfloag;
        private double propdouble;
        private byte propbyte;
        private Character propCharacter;
        private char propchar;
        private Boolean propBoolean;
        private boolean propboolean;
        private long proplong;
        private short propshort;
        private String name;
        private List<String> listString;

        OrderDTO orderDTO;

        Date gmtCreate;

        Set<OrderDTO> orderDTOSet;
    }

    @Data
    public static class OrderDTO implements Serializable {

        private String itemName;

        private Long orderId;

        private Integer orderNum;
    }

    public enum ColorEnum {
        RED,
        GREEN,
        BLUE
    }

    @Test
    public void testFury() {
        FuryBuilder builder = Fury.builder()
                .withLanguage(Language.JAVA)
                .withRefTracking(false)
                .requireClassRegistration(false);
        Fury fury = builder.build();
        for (int i = 0; i < 30; i++) {
            UserDTO dto = BeanMock.mockBean(UserDTO.class);
            dto.setColorEnum(ColorEnum.RED);
            long t1 = System.currentTimeMillis();
            byte[] bs = fury.serialize(dto);
            long t2 = System.currentTimeMillis();
            UserDTO dDto = (UserDTO) fury.deserialize(bs);
            long t3 = System.currentTimeMillis();
            assertEquals(dto, dDto);
            log.info("elapsed[{}]: serialize={}ms, deserialize={}ms", bs.length, t2 - t1, t3 - t2);
        }
    }
}
