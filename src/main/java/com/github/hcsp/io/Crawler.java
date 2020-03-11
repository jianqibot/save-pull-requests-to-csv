package com.github.hcsp.io;

import org.kohsuke.github.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Crawler {
    // 给定一个仓库名，例如"golang/go"，或者"gradle/gradle"，读取前n个Pull request并保存至csvFile指定的文件中，格式如下：
    // number,author,title
    // 12345,blindpirate,这是一个标题
    // 12345,FrankFang,这是第二个标题
    public static void savePullRequestsToCSV(String repo, int n, File csvFile) throws IOException {
        GitHub github = GitHub.connectAnonymously();
        GHRepository repository = github.getRepository(repo);
        List<GHPullRequest> pullRequests = repository.getPullRequests(GHIssueState.OPEN);

        String title = "number,author,title" + "\r\n";
        ArrayList<String> lines = new ArrayList<>();
        lines.add(title);
        lines.addAll(pullRequests.stream().map(Crawler::getLine).collect(Collectors.toList()));
        Files.write(csvFile.toPath(), lines);
    }

    private static String getLine(GHPullRequest ghPullRequest) {
        try {
            return ghPullRequest.getNumber() + "," + ghPullRequest.getUser().getLogin() + "," + ghPullRequest.getTitle();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
