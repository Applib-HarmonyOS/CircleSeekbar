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
 * UnionAbility to test the CircleSeekBar library functionality.
 */
public class UnionAbilitySlice extends AbilitySlice {

    private CircleSeekBar mHourSeekbar;

    private CircleSeekBar mMinuteSeekbar;

    private Text mTextView;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_union);
        mHourSeekbar = (CircleSeekBar) findComponentById(ResourceTable.Id_seek_hour);
        mMinuteSeekbar = (CircleSeekBar) findComponentById(ResourceTable.Id_seek_minute);
        mTextView = (Text) findComponentById(ResourceTable.Id_textview);
        mHourSeekbar.setOnSeekBarChangeListener(new CircleSeekBar.OnSeekBarChangeListener() {

            @Override
            public void onChanged(CircleSeekBar seekbar, int curValue) {
                changeText(curValue, mMinuteSeekbar.getCurProcess());
            }
        });
        mMinuteSeekbar.setOnSeekBarChangeListener(new CircleSeekBar.OnSeekBarChangeListener() {

            @Override
            public void onChanged(CircleSeekBar seekbar, int curValue) {
                changeText(mHourSeekbar.getCurProcess(), curValue);
            }
        });
        mHourSeekbar.setCurProcess(5);
        mMinuteSeekbar.setCurProcess(30);
    }

    private void changeText(int hour, int minute) {
        String hourStr = hour > 9 ? hour + "" : "0" + hour;
        String minuteStr = minute > 9 ? minute + "" : "0" + minute;
        String param = UnionAbilitySlice.changeParamToString(hourStr + ":" + minuteStr);
        mTextView.setText(param);
    }

    public static String changeParamToString(CharSequence charSequence) {
        return charSequence.toString();
    }
}
