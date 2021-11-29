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

package test.io.feeeei.circleseekbar.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DependentLayout;
import io.feeeei.circleseekbar.CircleSeekBar;

/**
 * InflateAbility to test the CircleSeekBar library functionality.
 */
public class InflateAbilitySlice extends AbilitySlice {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        DependentLayout dependentLayout = new DependentLayout(this);
        ComponentContainer.LayoutConfig config = new ComponentContainer.LayoutConfig(
                ComponentContainer.LayoutConfig.MATCH_PARENT,
                ComponentContainer.LayoutConfig.MATCH_PARENT);
        dependentLayout.setLayoutConfig(config);
        CircleSeekBar circleSeekBar = new CircleSeekBar(this);
        DependentLayout.LayoutConfig dependentLayoutConfig = new DependentLayout.LayoutConfig(800, 800);
        dependentLayoutConfig.addRule(DependentLayout.LayoutConfig.CENTER_IN_PARENT);
        circleSeekBar.setLayoutConfig(dependentLayoutConfig);
        dependentLayout.addComponent(circleSeekBar);
        setUIContent(dependentLayout);
    }
}
