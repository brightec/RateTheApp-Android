/*
 * Copyright 2016 Brightec Ltd
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

package uk.co.brightec.ratetheapp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultOnUserSelectedRatingListenerTest {

    private static final String FAKE_EMAIL_BODY = "EMAIL_BODY";
    private static final String FAKE_PACKAGE_NAME = "FAKE_PACKAGE_NAME";
    private static final String FAKE_DEVICE_NAME = "FAKE_DEVICE_NAME";

    @Mock
    Context mMockContext;
    @Mock
    PackageManager mMockPackageManager;
    @Mock
    PackageInfo mMockPackageInfo;
    @Mock
    Utils mMockUtils;

    @Test
    public void getDefaultEmailMessage_isCorrect() throws Exception {

//        when(mMockContext.getPackageManager()).thenReturn(mMockPackageManager);
//        when(mMockContext.getPackageName()).thenReturn(FAKE_PACKAGE_NAME);
//        when(mMockPackageManager.getPackageInfo(FAKE_PACKAGE_NAME, 0)).thenReturn(mMockPackageInfo);
//
//        when(mMockContext.getString(R.string.ratetheapp_feedback_extra_information)).thenReturn(FAKE_EMAIL_BODY);
//
//        DefaultOnUserSelectedRatingListener defaultOnUserSelectedRatingListener = DefaultOnUserSelectedRatingListener.createDefaultInstance(mMockContext);
//        Assert.assertEquals(FAKE_EMAIL_BODY, defaultOnUserSelectedRatingListener.getDefaultEmailMessage(mMockContext, 1));
    }
}
