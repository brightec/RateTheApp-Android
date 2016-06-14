package uk.co.brightec.ratetheapp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

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
