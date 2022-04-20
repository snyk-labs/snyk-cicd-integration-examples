# Azure Pipelines

## Inline display

Azure pipelines support the inline display of scan results via a [plugin][sarif-viewer-plugin]. 

![](azure-pipelines-inline-sarif-example.png)

## HTML reports

You can also use `snyk-to-html` to create HTML artifacts. They can be browsed using a [plugin][html-viewer-plugin] or downloaded from the pipeline.

![](azure-pipelines-html-report-example.png)

[sarif-viewer-plugin]: https://marketplace.visualstudio.com/items?itemName=sariftools.scans&targetId=bf3858e2-f2d0-4e06-962a-2107402a1234
[html-viewer-plugin]: https://marketplace.visualstudio.com/items?itemName=JakubRumpca.azure-pipelines-html-report