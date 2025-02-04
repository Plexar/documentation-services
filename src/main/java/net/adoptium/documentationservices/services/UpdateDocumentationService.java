package net.adoptium.documentationservices.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;

@ApplicationScoped
public class UpdateDocumentationService implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateDocumentationService.class);

    private final RepoService repoService;

    @Inject
    public UpdateDocumentationService(RepoService repoService) {
        this.repoService = repoService;
    }

    /**
     * This constructor is needed to not end in the WELD-001410 issue:
     * "WELD-001410: The injection point has non-proxyable dependencies"
     * Seehttp://stackoverflow.com/questions/12291945/ddg#34375558
     */
    public UpdateDocumentationService() {
        this(null);
    }

    /**
     * Main method which updates documentation from the repo if required.
     */
    public void updateDocumentationIfRequired() {
        LOG.info("Checking for update in documentation repository.");
        try {
            // check if there is something to do
            if (repoService.isUpdateAvailable()) {
                LOG.info("Starting documentation update.");
                // download files from repo
                repoService.downloadRepositoryContent();
                LOG.info("Finished documentation update.");
            }
        } catch (Exception e) {
            LOG.error("Encountered Exception while updating documentation.", e);
        }
    }
}
