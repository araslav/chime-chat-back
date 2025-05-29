package com.petpace.chat.aws.chime.service.impl;

import com.petpace.chat.aws.chime.service.RecordingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.chimesdkmediapipelines.ChimeSdkMediaPipelinesClient;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.ArtifactsConfiguration;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.ArtifactsState;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.AudioArtifactsConfiguration;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.AudioMuxType;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.ChimeSdkMeetingConfiguration;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.ContentArtifactsConfiguration;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.CreateMediaCapturePipelineRequest;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.CreateMediaCapturePipelineResponse;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.VideoArtifactsConfiguration;
import software.amazon.awssdk.services.chimesdkmediapipelines.model.VideoMuxType;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AwsRecordingServiceImpl implements RecordingService {
    private final ChimeSdkMediaPipelinesClient chimeSdkMediaPipelinesClient;

    public void startRecording(String meetingId) {
        CreateMediaCapturePipelineRequest request = CreateMediaCapturePipelineRequest.builder()
                .sourceType("ChimeSdkMeeting")
                .sourceArn("arn:aws:chime:us-east-1:127214159583:meeting:" + meetingId)
                .sinkType("S3Bucket")
                .sinkArn("arn:aws:s3:::mychatapp-fun-bucket")
                .chimeSdkMeetingConfiguration(ChimeSdkMeetingConfiguration.builder()
                        .artifactsConfiguration(ArtifactsConfiguration.builder()
                                .audio(AudioArtifactsConfiguration.builder()
                                        .muxType(AudioMuxType.AUDIO_WITH_COMPOSITED_VIDEO) // 👈 объединить аудио с видео
                                        .build())
                                .video(VideoArtifactsConfiguration.builder().state(ArtifactsState.ENABLED).muxType(VideoMuxType.VIDEO_ONLY).build())
                                .content(ContentArtifactsConfiguration.builder().state("Disabled").build())
                                .build())
                        .build())
                .clientRequestToken(UUID.randomUUID().toString())
                .build();

        CreateMediaCapturePipelineResponse response = chimeSdkMediaPipelinesClient.createMediaCapturePipeline(request);
        System.out.println("Pipeline ID: " + response.mediaCapturePipeline().mediaPipelineId());
        System.out.println("Status: " + response.mediaCapturePipeline().statusAsString());
        System.out.println("Source Type: " + response.mediaCapturePipeline().sourceTypeAsString());
        System.out.println("Sink Type: " + response.mediaCapturePipeline().sinkTypeAsString());
    }

    public void printCredentials() {
        AwsCredentialsProvider provider = DefaultCredentialsProvider.create();
        AwsCredentials credentials = provider.resolveCredentials();

        System.out.println("Access Key ID: " + credentials.accessKeyId());
        System.out.println("Secret Access Key: " + credentials.secretAccessKey());
    }
}
