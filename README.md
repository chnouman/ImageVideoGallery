# ImageVideoGallery

 
An android library for the quering MediaStore API to get media files i.e Images and Videos.





# How to use
Get latest version of Android studio and clone the repository.

	
# Usage 
The ImageVideoGalleryy library already contains a set of classes that can help you get the images and video files foleders and the content of these folders also.


## Getting images files from the MediaStore

**Methods to work with Images**
```java

//to get all images
allImages =     ImageVideGallery.
                withPictureContext(context)
                .getAllPictureContents

//to get images with the bucketId
imagesOfBucket = ImageVideGallery
                 .withPictureContext(context)
                 .getAllPictureContentByBucketId

//to get all the folder images with images included
allFolders =    ImageVideGallery
                .withPictureContext(context)
                .allPictureFolders

```

**Methods to work with Videos**
```java

//to get all videos
  allVideos =     ImageVideGallery
                  .withVideoContext(context)
                  .getAllVideoContent

//to get videos with the bucketId
videosOfBucket =  ImageVideGallery
                  .withVideoContext(context)
                  .getAllVideoContentByBucketId

//to get all the folder videos with videos included
allFolders =      ImageVideGallery
                  .withVideoContext(context)
                  .getAllVideoFolders

   ```

 
This repository also contains complete working example, how to use this library as a module to build a Custom Gallery App which shows Images and Videos to user in Customized design.


