/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test.io.feeeei.circleseekbar;

import io.feeeei.circleseekbar.CircleSeekBar;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.agp.components.Attr;
import ohos.agp.components.AttrSet;
import ohos.agp.utils.Color;
import ohos.app.Context;
import org.junit.Before;
import org.junit.Test;
import java.util.Optional;

import static org.junit.Assert.*;

public class ExampleOhosTest {
    private AttrSet attrSet;
    private Context context;
    private CircleSeekBar circleSeekBar;
    private static final String COLOR = "#4CAF50";
    private static final String COLOR1 = "#B7AC8D";

    @Before
    public void setUp() {
        context = AbilityDelegatorRegistry.getAbilityDelegator().getAppContext();
        attrSet = new AttrSet() {
            @Override
            public Optional<String> getStyle() {
                return Optional.empty();
            }

            @Override
            public int getLength() {
                return 0;
            }

            @Override
            public Optional<Attr> getAttr(int i) {
                return Optional.empty();
            }

            @Override
            public Optional<Attr> getAttr(String s) {
                return Optional.empty();
            }
        };
    }

    @Test
    public void testBundleName() {
        final String actualBundleName = AbilityDelegatorRegistry.getArguments().getTestBundleName();
        assertEquals("test.io.feeeei.circleseekbar", actualBundleName);
    }

    @Test
    public void testCurProcess() {
        circleSeekBar = new CircleSeekBar(context, attrSet);
        circleSeekBar.setCurProcess(5);
        assertEquals(5,circleSeekBar.getCurProcess());
    }

    @Test
    public void testMaxProcess() {
        circleSeekBar = new CircleSeekBar(context, attrSet);
        circleSeekBar.setMaxProcess(5);
        assertEquals(5,circleSeekBar.getMaxProcess());
    }

    @Test
    public void testReachedColor() {
        circleSeekBar = new CircleSeekBar(context, attrSet);
        circleSeekBar.setReachedColor(Color.getIntColor(COLOR));
        assertEquals(Color.getIntColor(COLOR), circleSeekBar.getReachedColor());
    }

    @Test
    public void testReachedColor1() {
        circleSeekBar = new CircleSeekBar(context, attrSet);
        circleSeekBar.setReachedColor(Color.getIntColor(COLOR));
        assertNotEquals(Color.getIntColor(COLOR1), circleSeekBar.getReachedColor());
    }

    @Test
    public void testUnreachedColor() {
        circleSeekBar = new CircleSeekBar(context, attrSet);
        circleSeekBar.setUnreachedColor(Color.getIntColor(COLOR));
        assertEquals(Color.getIntColor(COLOR), circleSeekBar.getUnreachedColor());
    }

    @Test
    public void testUnreachedColor1() {
        circleSeekBar = new CircleSeekBar(context, attrSet);
        circleSeekBar.setUnreachedColor(Color.getIntColor(COLOR));
        assertNotEquals(Color.getIntColor(COLOR1), circleSeekBar.getUnreachedColor());
    }

    @Test
    public void testReachedWidth() {
        circleSeekBar = new CircleSeekBar(context, attrSet);
        circleSeekBar.setReachedWidth(5f);
        assertEquals(String.valueOf(5f), String.valueOf(circleSeekBar.getReachedWidth()));
    }

    @Test
    public void testHasReachedCornerRound() {
        circleSeekBar = new CircleSeekBar(context, attrSet);
        circleSeekBar.setHasReachedCornerRound(true);
        assertEquals(true,circleSeekBar.isHasReachedCornerRound());
    }

    @Test
    public void testHasReachedCornerRound1() {
        circleSeekBar = new CircleSeekBar(context, attrSet);
        circleSeekBar.setHasReachedCornerRound(false);
        assertNotEquals(true,circleSeekBar.isHasReachedCornerRound());
    }

    @Test
    public void testUnreachedWidth() {
        circleSeekBar = new CircleSeekBar(context, attrSet);
        circleSeekBar.setUnreachedWidth(5f);
        assertEquals(String.valueOf(5f), String.valueOf(circleSeekBar.getUnreachedWidth()));
    }

    @Test
    public void testPointerColor() {
        circleSeekBar = new CircleSeekBar(context, attrSet);
        circleSeekBar.setPointerColor(Color.getIntColor(COLOR));
        assertEquals(Color.getIntColor(COLOR), circleSeekBar.getPointerColor());
    }

    @Test
    public void testPointerColor1() {
        circleSeekBar = new CircleSeekBar(context, attrSet);
        circleSeekBar.setPointerColor(Color.getIntColor(COLOR));
        assertNotEquals(Color.getIntColor(COLOR1), circleSeekBar.getPointerColor());
    }

    @Test
    public void testPointerRadius() {
        circleSeekBar = new CircleSeekBar(context, attrSet);
        circleSeekBar.setPointerRadius(5f);
        assertEquals(String.valueOf(5f), String.valueOf(circleSeekBar.getPointerRadius()));
    }

    @Test
    public void testWheelShadowRadius() {
        circleSeekBar = new CircleSeekBar(context, attrSet);
        assertNotNull(circleSeekBar.getWheelShadowRadius());
    }

    @Test
    public void testHasPointerShadow() {
        circleSeekBar = new CircleSeekBar(context, attrSet);
        assertNotNull(circleSeekBar.isHasPointerShadow());
    }

    @Test
    public void testPointerShadowRadius() {
        circleSeekBar = new CircleSeekBar(context, attrSet);
        circleSeekBar.setPointerShadowRadius(5f);
        assertEquals(String.valueOf(5f), String.valueOf(circleSeekBar.getPointerShadowRadius()));
    }

    @Test
    public void testChangeParamToColor() {
        Color hmosColor = CircleSeekBar.changeParamToColor(Color.RED.getValue());
        assertEquals(Color.RED,hmosColor);
    }

    @Test
    public void testChangeParamToColor1() {
        Color hmosColor = CircleSeekBar.changeParamToColor(Color.RED.getValue());
        assertNotEquals(Color.BLUE,hmosColor);
    }


}