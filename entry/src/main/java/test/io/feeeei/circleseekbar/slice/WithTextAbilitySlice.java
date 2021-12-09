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
import ohos.agp.components.Text;
import io.feeeei.circleseekbar.CircleSeekBar;
import test.io.feeeei.circleseekbar.ResourceTable;

/**
 * WithTextAbility to test the CircleSeekBar library functionality.
 */
public class WithTextAbilitySlice extends AbilitySlice {

    CircleSeekBar mSeekbar;
    private Text mTextView;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_with_text);
        mSeekbar = (CircleSeekBar) findComponentById(ResourceTable.Id_progress);
        mTextView = (Text) findComponentById(ResourceTable.Id_textview);
        mSeekbar.setOnSeekBarChangeListener(new CircleSeekBar.OnSeekBarChangeListener() {

            @Override
            public void onChanged(CircleSeekBar seekbar, int curValue) {
                mTextView.setText("value:" + curValue);
            }
        });
        mSeekbar.setCurProcess(0);
    }
}
