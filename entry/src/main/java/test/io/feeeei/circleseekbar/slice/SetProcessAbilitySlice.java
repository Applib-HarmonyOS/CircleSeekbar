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
import ohos.agp.components.Component;
import io.feeeei.circleseekbar.CircleSeekBar;
import test.io.feeeei.circleseekbar.ResourceTable;

/**
 * SetProcessAbility to test the CircleSeekBar library functionality.
 */
public class SetProcessAbilitySlice extends AbilitySlice {

    private CircleSeekBar mSeekbar;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_set_process);
        mSeekbar = (CircleSeekBar) findComponentById(ResourceTable.Id_seekbar);
        findComponentById(ResourceTable.Id_btn).setClickedListener(new Component.ClickedListener() {

            @Override
            public void onClick(Component v) {
                int process = mSeekbar.getCurProcess();
                process += 10;
                mSeekbar.setCurProcess(process);
            }
        });
    }
}
