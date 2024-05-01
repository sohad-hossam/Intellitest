return slicedScores.map(([key, value], index) => (
      <div key={index}>
        <ProgressBar label={key} score={value.maintainability_score} />
        <Card>
          <Card.Body>
            <Card.Title>Additional Information</Card.Title>
            <Card.Text>
              <strong>Halstead Volume:</strong> <br />
              n: {value.halstead_volume_results.n} <br />
              N: {value.halstead_volume_results.N} <br />
              N_hat: {value.halstead_volume_results.N_hat} <br />
              V: {value.halstead_volume_results.V} <br />
              D: {value.halstead_volume_results.D} <br />
              E: {value.halstead_volume_results.E} <br />
              T: {value.halstead_volume_results.T} <br />
              B: {value.halstead_volume_results.B} <br />
              <strong>SLOC and Comment Lines:</strong> <br />
              SLOC: {value.sloc_and_comment_lines_results.SLOC} <br />
              Comment Lines Ratio: {value.sloc_and_comment_lines_results.comment_lines_ratio} <br />
              <strong>Cyclomatic Complexity:</strong> <br />
              {value.cyclomatic_complexity}
            </Card.Text>
          </Card.Body>
        </Card>
      </div>
    ));
  };