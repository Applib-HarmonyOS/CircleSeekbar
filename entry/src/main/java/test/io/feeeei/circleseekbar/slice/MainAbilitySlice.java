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
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.ListContainer;
import ohos.agp.components.Text;
import test.io.feeeei.circleseekbar.ResourceTable;


/**
 * Sample app to test the CircleSeekBar library functionality.
 */
public class MainAbilitySlice extends AbilitySlice {

    ListContainer mListView;

    private Class[] mClasses = new Class[]{InflateAbilitySlice.class,
        Style1AbilitySlice.class, Style2AbilitySlice.class, WithShadowAbilitySlice.class,
        SetProcessAbilitySlice.class, WithTextAbilitySlice.class, DownLoadAbilitySlice.class, UnionAbilitySlice.class };

    private AbilitySlice[] mClassObjects = new AbilitySlice[]{new InflateAbilitySlice(),
        new Style1AbilitySlice(), new Style2AbilitySlice(), new WithShadowAbilitySlice(),
        new SetProcessAbilitySlice(), new WithTextAbilitySlice(), new DownLoadAbilitySlice(), new UnionAbilitySlice() };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_ability_main);
        mListView = (ListContainer) findComponentById(ResourceTable.Id_listview);
        mListView.setItemProvider(new BaseItemProvider() {

            @Override
            public int getCount() {
                return mClasses.length;
            }

            @Override
            public String getItem(int position) {
                return mClasses[position].getSimpleName();
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
                if (component == null) {
                    component = LayoutScatter.getInstance(MainAbilitySlice.this).parse(ResourceTable.Layout_list_item,
                            componentContainer, false);
                    ((Text) component.findComponentById(ResourceTable.Id_text)).setText(getItem(i));
                }
                return component;
            }
        });
        mListView.setItemClickedListener(new ListContainer.ItemClickedListener() {
            @Override
            public void onItemClicked(ListContainer listContainer, Component component, int i, long l) {
                present(mClassObjects[i], new Intent());
            }
        });
    }
}
