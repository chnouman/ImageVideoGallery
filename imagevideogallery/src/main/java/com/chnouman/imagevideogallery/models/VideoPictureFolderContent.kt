package com.chnouman.imagevideogallery.models

class VideoPictureFolderContent(
    folderPath: String?,
    folderName: String?,
    currentVideoFiles: MutableList<VideoContent>,
    pictureContents: MutableList<PictureContent>
) : FolderContent(folderPath, folderName) {
    var videoFiles = currentVideoFiles
    var photos = pictureContents
        private set

}